package cz.simt.tabule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.simt.tabule.data.Times;
import cz.simt.tabule.dto.GetPlayerIdDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.repository.PlayerRepository;

@Service
@EnableScheduling
public class PlayerService {
    private final TripService tripService;
    private final PlayerRepository playerRepository;
    private final GroupStationService groupStationService;
    private final ApiRead apiRead;
    private final TimesService timesService;
    private final RouteService routeService;
    private static final Logger logger = LoggerFactory.getLogger("PlayerService");

    @Autowired
    public PlayerService(TripService tripService, PlayerRepository playerRepository, GroupStationService groupStationService,
                         ApiRead apiRead, TimesService timesService, RouteService routeService) {
        this.tripService = tripService;
        this.playerRepository = playerRepository;
        this.groupStationService = groupStationService;
        this.apiRead = apiRead;
        this.timesService = timesService;
        this.routeService = routeService;
    }

    @Value("${app.apiurl.player}")
    private String apiUrl;

    @Scheduled(fixedRate = 10000)
    private void cronLoadPlayers() {
        logger.info("Cron to load players.");
        LocalDateTime pageLoaded = timesService.getTimeById("pageLoaded");
        if (pageLoaded == null || pageLoaded.plusMinutes(5).isBefore(LocalDateTime.now())) {
            logger.info("More than 5 minutes inactive. Skipping processing of players..");
        } else {
            loadPlayers();
        }
    }

    public void loadPlayers() {
        logger.info("Loading players started..");
        logger.info("Download players started..");
        String[] stringPlayer;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Future<String[]> future = executor.submit(() -> apiRead.readFromUrl(apiUrl));
        try {
            stringPlayer = future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            future.cancel(true);
            logger.error("Download players RESPONSE TIMEOUT. Skipping..\n" + ex.getMessage());
            return;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("CANNOT LOAD PLAYERS, SKIPPING..\n" + e.getMessage());
            e.printStackTrace();
            return;
        }
        logger.info("Download players DONE.");

        playerRepository.uncheckPlayers();
        LocalTime time = LocalTime.now();

        for (int i = 0; i < stringPlayer.length - 1; i++) {
            // System.out.println("Setting player: " + stringPlayer[i]);
            Player player = createPlayerFromPattern(stringPlayer[i]);
            if (player != null) {
                Optional<Player> optionalPlayer = playerRepository.findById(player.getId());

                if (optionalPlayer.isPresent()) {
                    Player playerDb = optionalPlayer.get();
                    if (!player.getTime().equals(playerDb.getTime())) {
                        playerDb.setStation(player.getStation());
                        playerDb.setTime(player.getTime());
                        tripService.unsetPosition(playerDb.getId());
                        tripService.setPosition(playerDb.getId(), playerDb.getStation());
                    }
                    if (!Objects.equals(playerDb.getStation(), player.getStation()) || playerDb.getDelay() != player.getDelay()) {
                        playerDb.setUpdated(LocalDateTime.now());
                    }
                    playerDb.setDelay(player.getDelay());
                    playerDb.setChecked('Y');
                    playerRepository.save(playerDb);
                } else if (!routeService.lineExist(player.getLine()) && !player.getLine().equals("0")) {
                    logger.warn("Cannot find line for player " + player.toString());
                } else {
                    player.setChecked('Y');
                    playerRepository.save(player);
                    tripService.loadTrip(player);
                    player.setEndStation(tripService.getLastStation(player.getId()).getStation());
                    playerRepository.save(player);
                    tripService.setPosition(player.getId(), player.getStation());
                    logger.debug("Save new player: " + stringPlayer[i]);
                }
            }
        }
        timesService.saveTime(new Times("playersJsonGenerated", LocalDateTime.parse(stringPlayer[stringPlayer.length-1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        List<Player> uncheckPlayers = playerRepository.findUnchecked();
        for (Player deletePlayer : uncheckPlayers) {
            tripService.deleteTrip(deletePlayer.getId());
            playerRepository.delete(deletePlayer);
            logger.debug("Delete player: " + deletePlayer.getId());
        }


        logger.info("Loading players DONE.\n=================================\n");
    }

    private Player createPlayerFromPattern(String player) {
        //18/B/Bělice/19:3/1130/2-0012/11/267
        /*
        23 - číslo linky
        B - směr linky
        Na Lukách - cílová zastávka
        9:45 - odjezd (výchozí čas)
        -1030 - zpoždění nebo nadjetí v sekundách vůči následující zastávce
        6-0025 - herní kód vozu (typ vozidla)
        13 - počet zastávek na lince
        287 - ID aktuální zastávky
         */
        String[] sp = player.split("/");
        if (sp.length == 9) {
            String[] st = sp[3].split(":");
            LocalDateTime startTime = LocalDate.now().atTime(Integer.parseInt(st[0]), Integer.parseInt(st[1]));
            startTime = (startTime.getHour() < 4 && LocalDateTime.now().isAfter(LocalDate.now().atTime(19, 0))) ? startTime.plusDays(1) : startTime;
            sp[7] += (groupStationService.determineTraction(sp[0])) ? ":2" : ":0";

            try {
                return new Player(getPlayerIdFromPattern(player), sp[0], sp[1], sp[5], startTime, getTimeFromDelay(sp[4]), sp[7], sp[2], Integer.parseInt(sp[4]), LocalDateTime.now(), sp[8]);
            } catch (Exception e) {
                logger.error("Error creating player " + player + " " + e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    private String getPlayerIdFromPattern(String player) {
        String[] splitPlayer = player.split("/");
        return splitPlayer[0] + splitPlayer[1] + splitPlayer[3] + splitPlayer[5] + splitPlayer[8];
    }

    private LocalTime getTimeFromDelay(String delay) {
        LocalTime time = LocalTime.now();
        time = time.plusSeconds(Integer.parseInt(delay));
        return time.truncatedTo(ChronoUnit.MINUTES);
    }

    public Player getPlayerFromId(String playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.orElse(new Player());
    }

    public List<GetPlayerIdDto> getAllPlayersId() {
        List<String> playersId = playerRepository.findAllPlayersId();
        List<GetPlayerIdDto> playersIdDto = new ArrayList<>();
        int cnt = 0;

        for (String playerId : playersId) {
            playersIdDto.add(new GetPlayerIdDto(cnt++, playerId));
        }

        return playersIdDto;
    }

    public void loadPlayerWhenInactive() {
        LocalDateTime loaded = timesService.getTimeById("pageLoaded");
        if (loaded == null || loaded.plusMinutes(5).isBefore(LocalDateTime.now())) {
            logger.info("Inactive load players.");
            loadPlayers();
        }
    }
}

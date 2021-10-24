package cz.simt.tabule.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.repository.PlayerRepository;

@Service
@EnableScheduling
public class PlayerService {
    private RawDataService rawDataService;
    private final TripService tripService;
    private final PlayerRepository playerRepository;
  //  private final StationService stationService;
    private final ApiRead apiRead;

    @Autowired
    public PlayerService(RawDataService rawDataService, TripService tripService, PlayerRepository playerRepository,
                        // StationService stationService,
                         ApiRead apiRead) {
        this.rawDataService = rawDataService;
        this.tripService = tripService;
        this.playerRepository = playerRepository;
      //  this.stationService = stationService;
        this.apiRead = apiRead;
    }

    @Scheduled(fixedRate=31000)
    public void loadPlayers() {
        System.out.println(LocalDateTime.now() + " Loading players started...");

        /*
        List<Player> players = rawDataService.getData();
        for (int i = 0; i<players.size(); i++) {
            if (players.get(i).getTime() == null) {
                players.remove(i);
                i--;
            }
        }

         */
        System.out.println(LocalDateTime.now() + " Download players started..");
        String[] stringPlayer;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Future<String[]> future = executor.submit(() -> apiRead.readFromUrl("https://simt.cz/server/dispData.php?kod=v5sov3b75sd"));
        try{
            stringPlayer = future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            future.cancel(true);
            System.out.println(LocalDateTime.now() + " Response Timeout.");
            return;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("CANNOT LOAD PLAYERS, SKIPPING..");
            e.printStackTrace();
            return;
        }
        System.out.println(LocalDateTime.now() + " Download players DONE.");

        playerRepository.uncheckPlayers();
        LocalTime time = LocalTime.now();

        for (int i = 0; i<stringPlayer.length-1; i++) {
           // System.out.println("Setting player: " + stringPlayer[i]);
            Player player = createPlayerFromPattern(stringPlayer[i]);
            if (player != null) {
                Optional<Player> optionalPlayer = playerRepository.findById(getPlayerIdFromPattern(stringPlayer[i]));

                if (optionalPlayer.isPresent()) {
                    Player playerDb = optionalPlayer.get();
                    if (!player.getTime().equals(playerDb.getTime())) {
                        playerDb.setStation(player.getStation());
                        playerDb.setTime(player.getTime());
                        tripService.unsetPosition(playerDb.getId());
                        tripService.setPosition(playerDb.getId(), playerDb.getStation());
                        System.out.println(LocalDateTime.now() + " Set player (" + playerDb.getId() + ") " + playerDb.getRoute() + " position: " +
                                //stationService.findStationName((long) playerDb.getStation(), playerDb.getLine())+
                                " (" + playerDb.getStation() + ") at " + playerDb.getTime());
                    }
                    if (!Objects.equals(playerDb.getStation(), player.getStation()) || playerDb.getDelay() != player.getDelay()) {
                        playerDb.setUpdated(LocalDateTime.now());
                    }
                    playerDb.setDelay(player.getDelay());
                    playerDb.setChecked('Y');
                    playerRepository.save(playerDb);
                } else {
                    player.setChecked('Y');
                    playerRepository.save(player);
                    tripService.loadTrip(player);
                    tripService.setPosition(player.getId(), player.getStation());
                    System.out.println(LocalDateTime.now() + " Save new player: " + stringPlayer[i]);
                }
            }
        }
        List<Player> uncheckPlayers = playerRepository.findUnchecked();
        for (Player deletePlayer : uncheckPlayers) {
            tripService.deleteTrip(deletePlayer.getId());
            playerRepository.delete(deletePlayer);
            System.out.println(LocalDateTime.now() + " Delete player: " + deletePlayer.getId());
        }

        System.out.println(LocalDateTime.now() + " Loading players ENDED.");
        System.out.println(LocalDateTime.now() + " =================================");
        System.out.println();
    }

    private Player createPlayerFromPattern(String player) {
        //18/B/Bělice/19:3/1130/2-0012/11/267
        String[] sp = player.split("/");
        if (sp.length == 8) {
            String[] st = sp[3].split(":");
            if (sp[0].length() == 1) {
                sp[7] += ":2";
            } else {
                sp[7] += ":0";
            }
            try {
                return new Player(getPlayerIdFromPattern(player), sp[0], sp[1], sp[5], LocalTime.of(Integer.parseInt(st[0]), Integer.parseInt(st[1])), getTimeFromDelay(sp[4]), sp[7], sp[2], Integer.parseInt(sp[4]), LocalDateTime.now());
            } catch (RuntimeException e) {
                System.out.println("");
                System.out.println("Error creating player " + player);
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private String getPlayerIdFromPattern(String player) {
        String[] splitPlayer = player.split("/");
        return splitPlayer[0]+splitPlayer[1]+splitPlayer[2]+splitPlayer[3]+splitPlayer[5];
    }

    private LocalTime getTimeFromDelay(String delay) {
        LocalTime time = LocalTime.now();
        time = time.plusSeconds( Integer.parseInt(delay));
        return time.truncatedTo(ChronoUnit.MINUTES);
    }

    public Player getPlayerFromId(String playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.orElse(null);
    }
}

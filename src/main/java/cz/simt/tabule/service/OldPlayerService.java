package cz.simt.tabule.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.repository.PlayersRepository;

@Service
@EnableScheduling
public class OldPlayerService {
    private final RawDataService rawDataService;
    private final PlayersRepository playersRepository;
    private final RoutesService routesService;
    private final StationService stationService;

    @Autowired
    public OldPlayerService(RawDataService rawDataService, PlayersRepository playersRepository, RoutesService routesService, StationService stationService) {
        this.rawDataService = rawDataService;
        this.playersRepository = playersRepository;
        this.routesService = routesService;
        this.stationService = stationService;
    }

 //   @PostConstruct
    public void savePlayersOnStartup() {
        List<Player> players = rawDataService.getData();
        for (Player player : players) {
            player.setTimeLoaded(LocalTime.now());
           // player.setUpdated('F');
            playersRepository.save(player);
        }
    }

 //   @Scheduled(fixedDelay=10000)
    /*
    public void periodicallyLoadPlayers() {
        System.out.println(LocalDateTime.now() + " Loading players....");
        List<Player> players = rawDataService.getData();
        playersRepository.uncheckPlayers();
       // List<Player> fromDb = (List<Player>) playersRepository.findAll();
        LocalTime time = LocalTime.now();

        for (Player player : players) {
            Optional<Player> optionalFromDb = playersRepository.findById(player.getId());
            if (optionalFromDb.isPresent()) {
                Player fromDb = optionalFromDb.get();
                if (fromDb.getRoute().equals(player.getRoute())) {
                    fromDb.setChecked('Y');
                }
                if (fromDb.getTime() != player.getTime() && fromDb.getChecked() == 'Y' && fromDb.getLoaded() == 'Y') {
                    fromDb.setTime(player.getTime());
                    routesService.uncheckPosition(fromDb.getId());
                    routesService.setPosition(fromDb.getId(), fromDb.getTime());
                    int position = routesService.findPlayerPosition(fromDb.getId());
                  //  System.out.println(LocalDateTime.now() + " Set player (" + fromDb.getId() + ") " + fromDb.getRoute());
                    if (position > 0) {
                        System.out.println(LocalDateTime.now() + " Set player (" + fromDb.getId() + ") " + fromDb.getRoute() + " position: "+ stationService.findStationName((long) position)+" ("+position+")");
                    } else {
                        System.out.println(LocalDateTime.now() + "NO POSITION FOUND FOR " + fromDb.getNick() + " ("+fromDb.getId()+"), " + fromDb.getRoute());
                    }

                }
                if (time.minusSeconds(50).isAfter(fromDb.getTimeLoaded()) && fromDb.getLoaded() == 'N') {
                    if (routesService.loadRoute(fromDb.getId(),fromDb.getRoute(),fromDb.getTime())) {
                        fromDb.setLoaded('Y');
                        System.out.println(LocalDateTime.now() + " Load player ("+fromDb.getId()+") route: " + fromDb.getRoute());
                    } else {
                        fromDb.setLoaded('F');
                        System.out.println(LocalDateTime.now() + " Load player FAILED ("+fromDb.getId()+") route: " + fromDb.getRoute());
                    }

                }
                playersRepository.save(fromDb);

                if (fromDb.getChecked() == 'N') {
                    routesService.deleteByPlayerId(fromDb.getId());
                    playersRepository.deleteById(fromDb.getId());
                    System.out.println(LocalDateTime.now() + " Delete player: " + fromDb.getId() + ", route: " + fromDb.getRoute());
                }
            } else {
                player.setTimeLoaded(time);
                player.setChecked('Y');
                player.setLoaded('N');
                playersRepository.save(player);
                System.out.println(LocalDateTime.now() + " Save new player: " + player.getId() + ", route: " + player.getRoute());
            }
        }
        System.out.println(LocalDateTime.now() + " Loading players ENDED.");
        System.out.println(LocalDateTime.now() + " =================================");
        System.out.println();

    }


     */

}

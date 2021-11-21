package cz.simt.tabule.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import cz.simt.tabule.data.Line;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.data.Station;
import cz.simt.tabule.data.Trip;
import cz.simt.tabule.dto.GetStationDto;
import cz.simt.tabule.repository.StationRepository;

import static java.lang.Math.abs;

@Service
public class StationService {
    private final ExcelRead excelRead;
    private final StationRepository stationRepository;
    private final ApiRead apiRead;
    private final GroupStationService groupStationService;
    private final TripService tripService;
    private final PlayerService playerService;

    @Autowired
    public StationService(ExcelRead excelRead, StationRepository stationRepository, ApiRead apiRead, GroupStationService groupStationService, TripService tripService, PlayerService playerService) {
        this.excelRead = excelRead;
        this.stationRepository = stationRepository;
        this.apiRead = apiRead;
        this.groupStationService = groupStationService;
        this.tripService = tripService;
        this.playerService = playerService;
    }

   // @PostConstruct
    public void loadStationsOnStartup() {

        Station station = new Station();
        Map<Integer, List<String>> stations = excelRead.getRoute("Stops");
        for (int i = 0; i<stations.size(); i++) {
            List<String> s = stations.get(i);
            station.setGameId((long) Double.parseDouble(s.get(1)));
            station.setStationName(s.get(0));
            stationRepository.save(station);
        }
    }

    public String findStationName(Long gameId, String line) {
        Optional<Station> s;
        if (line.length() == 1) {
            s = stationRepository.findByGameIdAndTraction(gameId, 2);
        } else {
            s = stationRepository.findByGameIdAndTraction(gameId, 0);
        }

        return s.map(Station::getStationName).orElse(null);
    }

    @PostConstruct
    public void getStationList() {
        String[] split;
        try {
            split = apiRead.readFromUrl("https://simt.cz/server/dispData.php?kod=9b6kqv04wc0");
        } catch (IOException e) {
            System.out.println("CANNOT LOAD ROUTES, SKIPPING..");
            e.printStackTrace();
            return;
        }

        for (int i = 0; i<split.length-1; i++) {
            Station station = new Station();
            String[] stationSplit = split[i].split("/");

            station.setGameId((long) Double.parseDouble(stationSplit[0]));
            station.setStationTraction(Integer.parseInt(stationSplit[1]));
            station.setStationName(stationSplit[2]);
            stationRepository.save(station);
        }
        List<Station> stationList = StreamSupport
                .stream(stationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        groupStationService.createGroupedStations(stationList);
    }


    public List<GetStationDto> getStationInfo(String stationUrlName) {
        List<Trip> tripInfo = tripService.getStationData(stationUrlName);
        List<GetStationDto> getStationDtos = new ArrayList<>();
        int index = 0;

        for (Trip trip: tripInfo) {
            Player player = playerService.getPlayerFromId(trip.getPlayerId());

            int playerPosition = tripService.getPositionByPlayerId(player.getId());
            int isAtStation = playerPosition == trip.getSequence() ? 1 : 0;
            int timeToDeparture = (int) LocalDateTime.now().until(trip.getTime(), ChronoUnit.MINUTES);
            Line line = new Line(player.getLine());
            timeToDeparture = player.getDelay() > 0 ? timeToDeparture : timeToDeparture-(player.getDelay()/60);

            if (playerPosition <= trip.getSequence() && player.getUpdated().isAfter(LocalDateTime.now().minusMinutes(15)) &&
                    trip.getTime().plusMinutes(90).isAfter(LocalDateTime.now()) && //čas po odjezdu
                    trip.getTime().minusMinutes(120).isBefore(LocalDateTime.now()))  //čas před odjezdem
            {
                int delayInMins = player.getDelay() < 0 ? abs(player.getDelay()/60) : 0;
                delayInMins = player.getUpdated().isAfter(LocalDateTime.now().minusMinutes(2)) ? delayInMins : -1;
                GetStationDto getStationDto = new GetStationDto(index++, player.getLine(), player.getRoute(), line.getTraction(), player.getEndStation(), player.getStation(), trip.getTime().format(DateTimeFormatter.ofPattern("HH:mm")), delayInMins, isAtStation, timeToDeparture);
                getStationDtos.add(getStationDto);
            }

        }
        return getStationDtos;
    }
}
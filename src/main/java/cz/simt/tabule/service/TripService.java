package cz.simt.tabule.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import cz.simt.tabule.data.Line;
import cz.simt.tabule.dto.GetTripDto;
import cz.simt.tabule.dto.GetPlayerIdDto;
import cz.simt.tabule.dto.GetTripHeaderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.data.Route;
import cz.simt.tabule.data.Trip;
import cz.simt.tabule.repository.TripRepository;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final RouteService routeService;
    private final GroupStationService groupStationService;
    private final PlayerService playerService;
    private static final Logger logger = LoggerFactory.getLogger("TripService");

    @Autowired
    public TripService(TripRepository tripRepository, RouteService routeService, GroupStationService groupStationService, @Lazy PlayerService playerService) {
        this.tripRepository = tripRepository;
        this.routeService = routeService;
        this.groupStationService = groupStationService;
        this.playerService = playerService;
    }

    public void loadTrip(Player player) {
        List<Route> station = routeService.getRoute(player.getLine(), player.getRoute());
        for (Route s : station) {
            LocalDateTime time = player.getStartTime();
            if (player.getStartTime().getHour() < 6 || player.getStartTime().getHour() > 19) {
                //noc
                time = time.plusMinutes(s.getTime_noc());
            } else if ((player.getStartTime().getHour() < 10 && player.getStartTime().getHour() > 5) || (player.getStartTime().getHour() < 17 && player.getStartTime().getHour() > 12)) {
                //spicka
                time = time.plusMinutes(s.getTime_spicka());
            } else {
                //sedlo
                time = time.plusMinutes(s.getTime_sedlo());

            }
            if (groupStationService.determineTraction(player.getLine())) {
                tripRepository.save(new Trip(s.getSequence(), player.getId(), s.getStation() + ":2", time));
            } else {
                tripRepository.save(new Trip(s.getSequence(), player.getId(), s.getStation() + ":0", time));
            }

        }

    }

    public List<GetTripHeaderDto> getTripHeader() {
        List<GetPlayerIdDto> playersId = playerService.getAllPlayersId();
        List<GetTripHeaderDto> tripsHeader = new ArrayList<>();

        for (GetPlayerIdDto pId : playersId) {
            String playerId = pId.getPlayerId();
            GetTripHeaderDto tripHeader = new GetTripHeaderDto();
            tripHeader.setId(playerId);
            tripHeader.setLine(new Line(playerService.getPlayerFromId(playerId).getLine()));
            tripHeader.setStartStation(getFirstStation(playerId));
            tripHeader.setEndStation(getLastStation(playerId));
            tripHeader.setActualStation(getActualStationFromPlayerTable(playerId));
            tripHeader.setDepartureFromActualStation(getDepartureFromCurrentStopByPlayerId(playerId));
            tripsHeader.add(tripHeader);
        }
        tripsHeader.sort(Comparator.comparing(GetTripHeaderDto::getLine));
        return tripsHeader;
    }

    public List<GetTripDto> getFullTrip(String id) {
        List<Trip> tripInfo = tripRepository.findByPlayerIdEqualsOrderBySequenceAsc(id);
        List<GetTripDto> tripDto = new ArrayList<>();

        for (Trip trip : tripInfo) {
            tripDto.add(new GetTripDto(groupStationService.getGroupStationById(trip.getStopId()).getName(), trip.getSequence(), trip.getPosition() ? 1 : 0, trip.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        }
        return tripDto;
    }

    public GetTripDto getFirstStation(String playerId) {
        try {
            Trip fs = tripRepository.findFirstByPlayerIdOrderBySequenceAsc(playerId);
            return new GetTripDto(groupStationService.getGroupStationById(fs.getStopId()).getName(), fs.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (NullPointerException e) {
            logger.error("Cannot load firstStation for: " + playerId + "\n" + e);
            return new GetTripDto();
        }

    }

    public GetTripDto getLastStation(String playerId) {
        try {
            Trip ls = tripRepository.findFirstByPlayerIdOrderBySequenceDesc(playerId);
            return new GetTripDto(groupStationService.getGroupStationById(ls.getStopId()).getName(), ls.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (NullPointerException e) {
            logger.error("Cannot load lastStation for: " + playerId + "\n" + e);
            return new GetTripDto();
        }
    }

    public GetTripDto getActualStation(String playerId) {
        String as = getCurrentStopByPlayerId(playerId);
        LocalDateTime ast = getDepartureFromCurrentStopByPlayerId(playerId);
        if (!as.isEmpty() && ast != null) {
            return  new GetTripDto(as, ast.format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            return new GetTripDto();
        }
    }

    private GetTripDto getActualStationFromPlayerTable(String playerId) {
        GetTripDto tripDto = new GetTripDto();
        LocalDateTime ast = getDepartureFromCurrentStopByPlayerId(playerId);
        String asId = playerService.getPlayerFromId(playerId).getStation();
        tripDto.setStation(asId != null ? groupStationService.getGroupStationById(asId).getName() : null);
        tripDto.setDeparture(ast != null ? ast.format(DateTimeFormatter.ofPattern("HH:mm")) : null);
        return tripDto;
    }

    public void unsetPosition(String playerId) {
        tripRepository.unsetPosition(playerId);
    }

    public void setPosition(String playerId, String station) {
        tripRepository.setPosition(playerId, station);
    }

    public void deleteTrip(String playerId) {
        tripRepository.deleteByPlayer(playerId);
    }

    public List<Trip> getStationData(String stationUrlName) {
        List<String> stations = groupStationService.getStationIdsByUrlName(stationUrlName);
        return tripRepository.findByStopIdInOrderByTimeAsc(stations);
    }

    public int getPositionByPlayerId(String playerId) {
        Optional<Integer> i = tripRepository.findSequenceByAPlayerId(playerId);
        return i.orElse(0);
    }

    private String getCurrentStopByPlayerId(String playerId) {
        Optional<String> i = tripRepository.findActualStation(playerId);
        if(i.isPresent()) {
            return groupStationService.getGroupStationById(i.get()).getName();
        }
        return "";
    }

    private LocalDateTime getDepartureFromCurrentStopByPlayerId(String playerId) {
        Optional<LocalDateTime> i = tripRepository.findActualDeparture(playerId);
        return i.orElse(null);
    }
}

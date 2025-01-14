package cz.simt.tabule.service;

import java.util.*;

import cz.simt.tabule.data.Line;
import cz.simt.tabule.data.Route;
import cz.simt.tabule.dto.GetGroupStationsDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.GroupStation;
import cz.simt.tabule.data.Station;
import cz.simt.tabule.repository.GroupStationRepository;

@Service
public class GroupStationService {
    private final GroupStationRepository groupStationRepository;
    private final RouteService routeService;

    @Autowired
    public GroupStationService(GroupStationRepository groupStationRepository, RouteService routeService) {
        this.groupStationRepository = groupStationRepository;
        this.routeService = routeService;
    }

    public void createGroupedStations(List<Station> stations) {
        groupStationRepository.deleteAll();
        int id = 1;
        for (Station station : stations) {
            if (station.getStationName().matches(".+ N[123]$")) {
                station.setStationName(station.getStationName().substring(0, station.getStationName().length()-3));
            }
            if (groupStationRepository.countByName(station.getStationName().toLowerCase()) == 0) {
                GroupStation gs = new GroupStation();
                gs.setId(id);
                gs.setName(station.getStationName());
                gs.setIds(station.getGameId() + ":" + station.getStationTraction());
                gs.setUrlName(WordUtils.capitalizeFully(StringUtils.stripAccents(station.getStationName())).replaceAll("\\s+", ""));
                groupStationRepository.save(gs);
                id++;
            } else {
                int stationId = groupStationRepository.findIdByName(station.getStationName().toLowerCase());
                Optional<GroupStation> optionalGroupStation = groupStationRepository.findById(stationId);
                if (optionalGroupStation.isPresent()) {
                    GroupStation gs = optionalGroupStation.get();
                    gs.setIds(gs.getIds() + "," + station.getGameId() + ":" + station.getStationTraction());
                    groupStationRepository.save(gs);
                }
            }
        }
        setLinesToStations();
    }

    public List<String> getStationIdsByUrlName(String urlName) {
        List<String> listStation = new java.util.ArrayList<>(Collections.emptyList());
        String[] stationSplit = groupStationRepository.findIdsByUrlName(urlName).split(",");
        listStation.addAll(Arrays.asList(stationSplit));
        return listStation;
    }

    public List<GetGroupStationsDto> getListOfStations() {
        Iterable<GroupStation> groupStations = groupStationRepository.findAllByOrderByNameAsc();
        List<GetGroupStationsDto> getGroupStationsDto = new ArrayList<>();
        for (GroupStation gs : groupStations) {
            List<Line> lines = new ArrayList<>(Collections.emptyList());
            if (gs.getLines() != null) {
                String[] linesSplit = gs.getLines().split(",");
                for (String line : linesSplit) {
                    lines.add(new Line(line, routeService.getLineTraction(line)));
                }
                Collections.sort(lines);
            }

            GetGroupStationsDto getGroupStationDto = new GetGroupStationsDto(gs.getId(), gs.getName(), gs.getUrlName(), lines);
            getGroupStationsDto.add(getGroupStationDto);
        }
        return getGroupStationsDto;
    }

    public GroupStation getGroupStationById(String id) {
        return groupStationRepository.findNameByStationId(id+"%", "%,"+id+"%", "%,"+id);
    }

    public String getGroupStationNameByUrlName(String urlName) {
        Optional<GroupStation> groupStation = groupStationRepository.findFirstByUrlName(urlName);
        if (groupStation.isPresent()) {
            return groupStation.get().getName();
        } else {
            return "";
        }
    }

    public String getGroupStationNameById(int id) {
        GroupStation groupStation = groupStationRepository.findFirstById(id);
        if (groupStation != null)
            return groupStation.getName();
        else
            return " ";
    }

    /**
     * Přiřazení linek ke stanicím
     */
    private void setLinesToStations() {
        List<Route> routes = routeService.getRoutesWithoutDepot();

        for (Route route : routes) {
            String stationIdWithTraction = (determineTraction(route.getLine())) ? route.getStation() + ":2" : route.getStation() + ":0";
            GroupStation groupStation = getGroupStationById(stationIdWithTraction);

            if (groupStation.getLines() == null || groupStation.getLines().isEmpty()) {
                groupStation.setLines(route.getLine());
                groupStationRepository.save(groupStation);
            } else if (!groupStation.getLines().contains(route.getLine())) {
                groupStation.setLines(groupStation.getLines()+","+route.getLine());
                groupStationRepository.save(groupStation);
            }
        }
    }

    /**
     *
     * @return true if tram, otherwise false
     */
    public boolean determineTraction(String line) {
        return isInteger(line) && line.length() == 1;
    }

    private static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}

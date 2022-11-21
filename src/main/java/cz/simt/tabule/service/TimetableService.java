package cz.simt.tabule.service;

import cz.simt.tabule.data.Route;
import cz.simt.tabule.data.Timetable;
import cz.simt.tabule.dto.timetable.RoutesDto;
import cz.simt.tabule.dto.timetable.StationsDto;
import cz.simt.tabule.dto.timetable.TimelinesDto;
import cz.simt.tabule.repository.TimetableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final RouteService routeService;
    private final GroupStationService gss;
    private static final Logger logger = LoggerFactory.getLogger("TimetableService");

    @Autowired
    public TimetableService(TimetableRepository timetableRepository, RouteService routeService, GroupStationService gss) {
        this.timetableRepository = timetableRepository;
        this.routeService = routeService;
        this.gss = gss;
    }

    public void createTimetable() {
        // SELECT t.*, g.name FROM TIMETABLE t JOIN GROUP_STATION g ON g.id = t.GROUP_STATION
        logger.info("Creating Timelines started..");
        List<String> lines = routeService.getAllLines();


        for (String line : lines) {
            List<Timetable> directionA = new java.util.ArrayList<>(Collections.emptyList());
            List<Timetable> directionB = Collections.emptyList();
            List<String> directions = routeService.getDirectionsForLine(line);

            for (String direction : directions) {
                List<Route> routeList = routeService.getRoute(line, direction);

                //směr A/C/E/
                //pokud je znak lichý a dlouhý 1 znak nebo na druhé pozici lichý
                if (isADirection(direction)) {
                    directionA = new ArrayList<>(createFullDirection(directionA, routeList, "A"));
                } else if (isBDirection(direction)) {
                    directionB = new ArrayList<>(createFullDirection(directionB, routeList, "B"));
                } else {
                    logger.warn("Cannot assignee:" + line + "/" + direction);
                }
            }
            int indexA = 0, indexB = 0;
            for (String direction : directions) {

                List<Route> routeList = routeService.getRoute(line, direction);


                if (isADirection(direction)) {
                    directionA = new ArrayList<>(fillTimes(directionA, routeList, indexA));
                    indexA++;
                } else if (isBDirection(direction)) {
                    directionB = new ArrayList<>(fillTimes(directionB, routeList, indexB));
                    indexB++;
                }
            }
            saveTimetable(directionA);
            saveTimetable(directionB);
        }
        logger.info("Creating Timelines DONE.");
    }

    private List<Timetable> createFullDirection(List<Timetable> timetable, List<Route> routeList, String direction) {
        List<Timetable> proceed = new java.util.ArrayList<>(Collections.emptyList());
        if (timetable.isEmpty()) {
            for(Route route : routeList) {
                int groupStationId = gss.getGroupStationById(route.getStationWithTraction()).getId();
                Timetable row = new Timetable(route.getLine(), direction, groupStationId, "", "", "");
                proceed.add(row);
            }
        } else {
            proceed = timetable;
            int index = 0;
            for (Route route : routeList) {

                int groupStationId = gss.getGroupStationById(route.getStationWithTraction()).getId();
                Timetable row = new Timetable(route.getLine(), direction, groupStationId, "", "", "");

                if (timetable.stream().noneMatch(o -> o.getGroup_station() == row.getGroup_station())) {
                    proceed.add(index, row);
                }
                index++;
            }
        }
        return proceed;
    }

    private List<Timetable> fillTimes(List<Timetable> timetables, List<Route> routeList, int index) {
        List<Timetable> proceed = new java.util.ArrayList<>(Collections.emptyList());
        for (int i = 0; i< timetables.size(); i++) {
            Timetable timetable = timetables.get(i);
            List<Route> row = routeList.stream().filter(o -> gss.getGroupStationById(o.getStationWithTraction()).getId() == timetable.getGroup_station()).collect(Collectors.toList());
            if (!row.isEmpty()) {
                timetable.setTime_spicka(timetable.getTime_spicka() + row.get(0).getTime_spicka() + ",");
                timetable.setTime_sedlo(timetable.getTime_sedlo() + row.get(0).getTime_sedlo() + ",");
                timetable.setTime_noc(timetable.getTime_noc() + row.get(0).getTime_noc() + ",");
            } else {
                timetable.setTime_spicka(timetable.getTime_spicka() + "...,");
                timetable.setTime_sedlo(timetable.getTime_sedlo() + "...,");
                timetable.setTime_noc(timetable.getTime_noc() + "...,");
            }

            timetable.setSequence(i);
            proceed.add(timetable);
        }
        return proceed;
    }

    /**
     * směr A/C/E/ZA/XA
     * pokud je znak lichý a dlouhý 1 znak nebo na druhé pozici lichý
     * @param direction směr
     * @return boolean
     */
    private boolean isADirection(String direction) {
        return ((int) direction.charAt(0) % 2 != 0 && direction.length() == 1) || (direction.length() == 2 && (int) direction.charAt(1) % 2 != 0);
    }

    private boolean isBDirection(String direction) {
        return ((int) direction.charAt(0) % 2 == 0 && direction.length() == 1) || (direction.length() == 2 && (int) direction.charAt(1) % 2 == 0);
    }

    public void saveTimetable(List<Timetable> timetables) {
        timetableRepository.saveAll(timetables);
    }

    public List<TimelinesDto> getTimelines() {
        List<Timetable> timetables = timetableRepository.findAll();
        List<String> routeLines = routeService.getAllLines();
        List<TimelinesDto> lines = new ArrayList<>();

        for (String routeLine : routeLines) {
            List<Timetable> routes = timetables.stream().filter(i -> i.getLine().equals(routeLine)).collect(Collectors.toList());
            List<StationsDto> directionA = new ArrayList<>();
            List<StationsDto> directionB = new ArrayList<>();
            List<RoutesDto> fullLine = new ArrayList<>();

            for (Timetable ts : routes) {
                if (ts.getDirection().equals("A")) {
                    directionA.add(new StationsDto(
                            gss.getGroupStationNameById(ts.getGroup_station()),
                            ts.getSequence(),
                            Arrays.asList(ts.getTime_spicka().split(",")),
                            Arrays.asList(ts.getTime_sedlo().split(",")),
                            Arrays.asList(ts.getTime_noc().split(",")))
                    );
                } else if (ts.getDirection().equals("B")) {
                    directionB.add(new StationsDto(
                            gss.getGroupStationNameById(ts.getGroup_station()),
                            ts.getSequence(),
                            Arrays.asList(ts.getTime_spicka().split(",")),
                            Arrays.asList(ts.getTime_sedlo().split(",")),
                            Arrays.asList(ts.getTime_noc().split(",")))
                    );
                }
            }

            fullLine.add(new RoutesDto("A", directionA));
            fullLine.add(new RoutesDto("B", directionB));
            lines.add(new TimelinesDto(routeLine, fullLine, directionA.get(0).getName(), directionA.get(directionA.size()-1).getName()));
        }
        return lines;
    }

}

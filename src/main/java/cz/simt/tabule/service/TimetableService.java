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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        timetableRepository.deleteAll();

        List<String> lines = routeService.getAllLines();


        for (String line : lines) {
            List<Timetable> directionA = new java.util.ArrayList<>(Collections.emptyList());
            List<Timetable> directionB = new java.util.ArrayList<>(Collections.emptyList());
            List<Timetable> fullRoute = new java.util.ArrayList<>(Collections.emptyList());
            List<String> directions = routeService.getDirectionsForLine(line);

            for (String direction : directions) {
                List<Route> routeList = routeService.getRoute(line, direction);

                //směr A/C/E/
                //pokud je znak lichý a dlouhý 1 znak nebo na druhé pozici lichý
                if (isADirection(direction)) {
                    directionA = new ArrayList<>(createFullDirection(directionA, routeList, direction));
                } else if (isBDirection(direction)) {
                    directionB = new ArrayList<>(createFullDirection(directionB, routeList, direction));
                } else {
                    logger.warn("Cannot assignee:" + line + "/" + direction);
                }
            }

            int lineLength = Math.max(directionA.size(), directionB.size());

            for (int i = 0; i< lineLength; i++) {
                String da = directionA.size()>i ? gss.getGroupStationNameById(directionA.get(i).getGroup_station()) +" ("+directionA.get(i).getGroup_station()+ ")" : "0";
                String db = directionB.size()>i ? gss.getGroupStationNameById(directionB.get(i).getGroup_station()) +" ("+directionB.get(i).getGroup_station()+ ")" : "0";
            }


            Collections.reverse(directionB);
            int index = 0;
            while (!directionB.isEmpty()) {
                List<Timetable> finalDirectionA = directionA;
                List<Timetable> finalDirectionB = directionB;

                int indexOfAStation = IntStream.range(0, finalDirectionA.size())
                        .filter(o -> finalDirectionA.get(o).getGroup_station() == finalDirectionB.get(0).getGroup_station())
                        .findFirst().orElse(-1);
                if (index < directionA.size() && directionA.get(index).getGroup_station() == directionB.get(0).getGroup_station()) {
                    directionB.remove(0);
                    index++;
                } else if (indexOfAStation == -1 || indexOfAStation < index) {
                    directionA.add(index, directionB.get(0));
                    directionB.remove(0);
                    index++;
                } else {
                    index++;
                }
            }

            for (String direction : directions) {

                List<Route> routeList = routeService.getRoute(line, direction);

                if (isADirection(direction) || isBDirection(direction)) {
                    directionA = new ArrayList<>(fillTimes(directionA, routeList, direction));
                }
            }

            saveTimetable(directionA);
        }
        logger.info("Creating Timelines DONE.");
    }

    /**
     *
     * @param fullRoute already created route
     * @param routeList route with actual direction to check (A/B/C/D)
     * @param direction A/B/XA/XB/ZA/ZB
     * @return extended route of inputFullRoute
     */
    private List<Timetable> createFullDirection(List<Timetable> fullRoute, List<Route> routeList, String direction) {
        List<Timetable> positionOfStart = fullRoute.stream().filter(o -> o.getGroup_station() == gss.getGroupStationById(routeList.get(0).getStationWithTraction()).getId()).collect(Collectors.toList());
        int index = positionOfStart.size() == 1 ? fullRoute.indexOf(positionOfStart.get(0)) : 0;

        for (int i = 0; i < routeList.size(); i++) {
            Timetable row = new Timetable(routeList.get(i).getLine(), gss.getGroupStationById(routeList.get(i).getStationWithTraction()).getId(), "", "", "", "", "", "");

            if (fullRoute.isEmpty() || index >= fullRoute.size() || fullRoute.get(index).getGroup_station() != row.getGroup_station()) {
                int indexOfStation = IntStream.range(0, fullRoute.size())
                        .filter(o -> fullRoute.get(o).getGroup_station() == row.getGroup_station())
                        .findFirst().orElse(-1);

                if (indexOfStation <= index || indexOfStation == -1) {
                    fullRoute.add(index, row);
                }
            }
            index++;
        }
        return fullRoute;
    }

    /**
     *
     * @param fullRoute full line
     * @param routeList line/route to insert
     * @param direction
     * @return
     */
    private List<Timetable> fillTimes(List<Timetable> fullRoute, List<Route> routeList, String direction) {
        if (isADirection(direction)) {
            int indexRouteList = 0;
            for (int i = 0; i< fullRoute.size(); i++) {
                Timetable timetable = fullRoute.get(i);
                if (indexRouteList<routeList.size() && fullRoute.get(i).getGroup_station() == gss.getGroupStationById(routeList.get(indexRouteList).getStationWithTraction()).getId()) {
                    fullRoute.get(i).setTime_spicka_A(timetable.getTime_spicka_A() + routeList.get(indexRouteList).getTime_spicka() + ",");
                    fullRoute.get(i).setTime_sedlo_A(timetable.getTime_sedlo_A() + routeList.get(indexRouteList).getTime_sedlo() + ",");
                    fullRoute.get(i).setTime_noc_A(timetable.getTime_noc_A() + routeList.get(indexRouteList).getTime_noc() + ",");
                    indexRouteList++;
                } else if (indexRouteList<routeList.size() && indexRouteList>0) {
                    fullRoute.get(i).setTime_spicka_A(timetable.getTime_spicka_A() + "↓,");
                    fullRoute.get(i).setTime_sedlo_A(timetable.getTime_sedlo_A() + "↓,");
                    fullRoute.get(i).setTime_noc_A(timetable.getTime_noc_A() + "↓,");
                } else {
                    fullRoute.get(i).setTime_spicka_A(timetable.getTime_spicka_A() + "...,");
                    fullRoute.get(i).setTime_sedlo_A(timetable.getTime_sedlo_A() + "...,");
                    fullRoute.get(i).setTime_noc_A(timetable.getTime_noc_A() + "...,");
                }
                fullRoute.get(i).setSequence(i);
            }
        } else if (isBDirection(direction)) {
            int indexRouteList = routeList.size()-1;
            for (int i = 0; i< fullRoute.size(); i++) {
                Timetable timetable = fullRoute.get(i);
                if (indexRouteList>=0 && fullRoute.get(i).getGroup_station() == gss.getGroupStationById(routeList.get(indexRouteList).getStationWithTraction()).getId()) {
                    fullRoute.get(i).setTime_spicka_B(timetable.getTime_spicka_B() + routeList.get(indexRouteList).getTime_spicka() + ",");
                    fullRoute.get(i).setTime_sedlo_B(timetable.getTime_sedlo_B() + routeList.get(indexRouteList).getTime_sedlo() + ",");
                    fullRoute.get(i).setTime_noc_B(timetable.getTime_noc_B() + routeList.get(indexRouteList).getTime_noc() + ",");
                    indexRouteList--;
                } else if (indexRouteList<routeList.size()-1 && indexRouteList>0) {
                    fullRoute.get(i).setTime_spicka_B(timetable.getTime_spicka_B() + "↑,");
                    fullRoute.get(i).setTime_sedlo_B(timetable.getTime_sedlo_B() + "↑,");
                    fullRoute.get(i).setTime_noc_B(timetable.getTime_noc_B() + "↑,");
                } else {
                    fullRoute.get(i).setTime_spicka_B(timetable.getTime_spicka_B() + "...,");
                    fullRoute.get(i).setTime_sedlo_B(timetable.getTime_sedlo_B() + "...,");
                    fullRoute.get(i).setTime_noc_B(timetable.getTime_noc_B() + "...,");

                }
            }
        }

        return fullRoute;
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
            List<StationsDto> line = new ArrayList<>();
            List<RoutesDto> fullLine = new ArrayList<>();

            for (Timetable ts : routes) {
                line.add(new StationsDto(
                        gss.getGroupStationNameById(ts.getGroup_station()),
                        ts.getSequence(),
                        Arrays.asList(ts.getTime_spicka_A().split(",")),
                        Arrays.asList(ts.getTime_sedlo_A().split(",")),
                        Arrays.asList(ts.getTime_noc_A().split(",")),
                        Arrays.asList(ts.getTime_spicka_B().split(",")),
                        Arrays.asList(ts.getTime_sedlo_B().split(",")),
                        Arrays.asList(ts.getTime_noc_B().split(",")))
                );
            }

            lines.add(new TimelinesDto(routeLine, line, line.get(0).getName(), line.get(line.size()-1).getName()));
        }
        return lines;
    }

}

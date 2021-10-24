package cz.simt.tabule.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Trip;

@Service
@EnableScheduling
public class RoutesService {
    /*
    private final ExcelRead excelRead;
    private final RoutesRepository routesRepository;

    @Autowired
    public RoutesService(ExcelRead excelRead, RoutesRepository routesRepository) {
        this.excelRead = excelRead;
        this.routesRepository = routesRepository;
    }

    public boolean loadRoute(String playerId, String route, LocalTime start) {
        Map<Integer, List<String>> fullRoute = excelRead.getRoute(route);
        if (fullRoute != null) {
            for (int i = 0; i<fullRoute.size(); i++) {
                List<String> s = fullRoute.get(i);
                LocalTime stopTime = null;
                if ((start.isAfter(LocalTime.parse("08:00"))) && (start.isBefore(LocalTime.parse("20:00")))) {
                    stopTime = start.plusMinutes((int) Double.parseDouble(s.get(3)));
                } else {
                    stopTime = start.plusMinutes((int) Double.parseDouble(s.get(4)));
                }
                Trip stop = new Trip((int) Double.parseDouble(s.get(0)), playerId, (int) Double.parseDouble(s.get(1)), stopTime);

                routesRepository.save(stop);
            }
            return true;
        } else {
            return false;
        }
    }

    public void uncheckPosition(String id) {
        routesRepository.uncheckPosition(id);
    }

    public void setPosition(String id, LocalTime time) {
        routesRepository.setPosition(id, time);
    }

    public void deleteByPlayerId(String playerID) {
        routesRepository.deleteByPlayerId(playerID);
    }

    public int findPlayerPosition(String id) {
        List<Integer> k = routesRepository.findPlayerPosition(id);
        if (k.size() == 1) {
            return k.get(0);
        } else return 0;
    }

     */

}

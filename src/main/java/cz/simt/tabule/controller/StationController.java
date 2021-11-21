package cz.simt.tabule.controller;

import cz.simt.tabule.data.Times;
import cz.simt.tabule.service.GroupStationService;
import cz.simt.tabule.service.TimesService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.simt.tabule.service.StationService;

import java.time.LocalDateTime;

@Controller
public class StationController {
    private final StationService stationService;
    private final GroupStationService groupStationService;
    private final TimesService timesService;

    @Autowired
    public StationController(StationService stationService, GroupStationService groupStationService, TimesService timesService) {
        this.stationService = stationService;
        this.groupStationService = groupStationService;
        this.timesService = timesService;
    }

    @GetMapping("/zastavky")
    @CrossOrigin(origins = "http://localhost:8081")
    public @ResponseBody String getListOfStations() {
        System.out.println("GetListOfStations");
        return new JSONObject()
                .put("stations", groupStationService.getListOfStations())
                .toString();
    }
    

    @GetMapping("/zastavky/{stationUrlName}")
    @CrossOrigin(origins = "http://localhost:8081")
    public @ResponseBody String getStationData(@PathVariable final String stationUrlName) {
        timesService.saveCurrentTime("stationCalled");
        return new JSONObject()
                .put("stationName", groupStationService.getGroupStationNameByUrlName(stationUrlName))
                .put("departures", stationService.getStationInfo(stationUrlName))
                .toString();
    }
}

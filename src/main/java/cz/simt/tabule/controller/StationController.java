package cz.simt.tabule.controller;

import cz.simt.tabule.service.GroupStationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.simt.tabule.service.StationService;

@Controller
public class StationController {
    private final StationService stationService;
    private final GroupStationService groupStationService;

    @Autowired
    public StationController(StationService stationService, GroupStationService groupStationService) {
        this.stationService = stationService;
        this.groupStationService = groupStationService;
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
        System.out.println("GetStationInfo");
        return new JSONObject()
                .put("stationName", groupStationService.getStationNameByUrlName(stationUrlName))
                .put("departures", stationService.getStationInfo(stationUrlName))
                .toString();
    }
}

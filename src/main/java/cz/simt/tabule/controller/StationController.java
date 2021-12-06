package cz.simt.tabule.controller;

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
    @CrossOrigin(origins = "https://klobasa.github.io/")
    public @ResponseBody String getListOfStations() {
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("stations", groupStationService.getListOfStations())
                .toString();
    }

    @GetMapping("/zastavky/{stationUrlName}")
    @CrossOrigin(origins = "https://klobasa.github.io/")
    public @ResponseBody String getStationData(@PathVariable final String stationUrlName) {
        timesService.saveCurrentTime("stationCalled");
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("stationName", groupStationService.getGroupStationNameByUrlName(stationUrlName))
                .put("departures", stationService.getStationInfo(stationUrlName))
                .toString();
    }
}

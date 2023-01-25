package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayerService;
import cz.simt.tabule.service.TimesService;
import cz.simt.tabule.service.TripService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TripController {
    private final TripService tripService;
    private final PlayerService playerService;
    private final TimesService timesService;

    @Autowired
    public TripController(TripService tripService, PlayerService playerService, TimesService timesService) {
        this.tripService = tripService;
        this.playerService = playerService;
        this.timesService = timesService;
    }

    @GetMapping(value = "/spoj", produces = "application/json; charset=utf-8")
    public @ResponseBody String getTripList(@RequestParam(name = "man", required = false, defaultValue = "true") boolean manipulacni) {
        playerService.loadPlayerWhenInactive();
        timesService.saveCurrentTime("tripCalled");
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("tripHeader", tripService.getTripHeader(manipulacni))
                .put("timeGenerated", timesService.getTimeById("playersJsonGenerated"))
                .toString();

    }

    @GetMapping(value = "/spoj/{playerId}", produces = "application/json; charset=utf-8")
    public @ResponseBody String getFullTrip(@PathVariable final String playerId) {
        playerService.loadPlayerWhenInactive();
        timesService.saveCurrentTime("tripCalled");
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("line", playerService.getPlayerFromId(playerId).getLine())
                .put("endStation", tripService.getLastStation(playerId).getStation())
                .put("dataGenerated", timesService.getTimeById("playersJsonGenerated"))
                .put("trip", tripService.getFullTrip(playerId))
                .toString();
    }
}

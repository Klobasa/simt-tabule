package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayerService;
import cz.simt.tabule.service.TimesService;
import cz.simt.tabule.service.TripService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
    @GetMapping("/spoj")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getTripList() {
        timesService.saveCurrentTime("tripCalled");
        timesService.saveCurrentTime("pageLoaded");

    }
     */

    @GetMapping("/spoj")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getTripList() {
        timesService.saveCurrentTime("tripCalled");
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("tripHeader", tripService.getTripHeader())
                .toString();

    }

    @GetMapping("/spoj/{playerId}")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getFullTrip(@PathVariable final String playerId) {
        timesService.saveCurrentTime("tripCalled");
        timesService.saveCurrentTime("pageLoaded");
        return new JSONObject()
                .put("line", playerService.getPlayerFromId(playerId).getLine())
                .put("endStation", tripService.getLastStation(playerId))
                .put("trip", tripService.getFullTrip(playerId))
                .toString();
    }
}

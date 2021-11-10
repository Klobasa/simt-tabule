package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayerService;
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

    @Autowired
    public TripController(TripService tripService, PlayerService playerService) {
        this.tripService = tripService;
        this.playerService = playerService;
    }

    @GetMapping("/spoj/{playerId}")
    @CrossOrigin(origins = "http://localhost:8081")
    public @ResponseBody String getFullTrip(@PathVariable final String playerId) {
        return new JSONObject()
                .put("line", playerService.getPlayerFromId(playerId).getLine())
                .put("endStation", tripService.getLastStation(playerId))
                .put("trip", tripService.getFullTrip(playerId))
                .toString();
    }
}

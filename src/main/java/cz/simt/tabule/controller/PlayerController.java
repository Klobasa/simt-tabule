package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/playersId", produces = "application/json; charset=utf-8")
    public @ResponseBody String getPlayersId() {
        return new JSONObject()
                .put("players", playerService.getAllPlayersId())
                .toString();
    }
}

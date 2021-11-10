package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/playersId")
    @CrossOrigin(origins = "http://localhost:8081")
    public @ResponseBody String getPlayersId() {
        System.out.println("PlayersId");
        return new JSONObject()
                .put("players", playerService.getAllPlayersId())
                .toString();
    }
}

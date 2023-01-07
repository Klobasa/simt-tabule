package cz.simt.tabule.controller;

import cz.simt.tabule.service.PlayersListService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayersListController {
    private final PlayersListService playersListService;

    @Autowired
    public PlayersListController(PlayersListService playersListService) {
        this.playersListService = playersListService;
    }

    @GetMapping("/playersList")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getPlayersList() {
        return new JSONObject()
                .put("playersList", playersListService.getPlayersList())
                .toString();
    }
}
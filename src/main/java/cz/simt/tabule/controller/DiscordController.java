package cz.simt.tabule.controller;

import cz.simt.tabule.service.DiscordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DiscordController {
    private final DiscordService discordService;

    @Autowired
    public DiscordController(DiscordService discordService) {
        this.discordService = discordService;
    }

    @GetMapping("/discord/code")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getCode(@RequestParam String code) {
        return discordService.authenticate(code);
    }
}

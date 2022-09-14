package cz.simt.tabule.controller;

import cz.simt.tabule.dto.timetable.TimelinesDto;
import cz.simt.tabule.service.TimetableService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TimetableController {
    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/jizdnidoby")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getTimetables() {
        List<TimelinesDto> a = timetableService.getTimelines();
        return new JSONObject().put("timelines", a).toString();
    }
}

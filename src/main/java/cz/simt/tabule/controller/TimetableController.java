package cz.simt.tabule.controller;

import cz.simt.tabule.service.TimesService;
import cz.simt.tabule.service.TimetableService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TimetableController {
    private final TimetableService timetableService;
    private final TimesService timesService;

    @Autowired
    public TimetableController(TimetableService timetableService, TimesService timesService) {
        this.timetableService = timetableService;
        this.timesService = timesService;
    }

    @GetMapping("/jizdnidoby")
    @CrossOrigin(origins = {"https://klobasa.github.io/", "http://localhost:8081/"})
    public @ResponseBody String getTimetables() {
        return new JSONObject()
                .put("dataGenerated", timesService.getTimeById("routesJsonGenerated"))
                .put("timelines", timetableService.getTimelines())
                .toString();
    }
}

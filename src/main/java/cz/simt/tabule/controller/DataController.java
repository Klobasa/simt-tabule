package cz.simt.tabule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.simt.tabule.service.RawDataService;

@RestController
public class DataController {
    private final RawDataService rawDataService;

    @Autowired
    public DataController(RawDataService rawDataService) {
        this.rawDataService = rawDataService;
    }


}

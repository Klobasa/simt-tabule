package cz.simt.tabule.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.service.ExcelRead;
import cz.simt.tabule.service.RawDataService;

@Controller
public class HomeController  {

    private final RawDataService rawDataService;
    private final ExcelRead excelRead;

    @Autowired
    public HomeController(RawDataService rawDataService, ExcelRead excelRead) {
        this.rawDataService = rawDataService;
        this.excelRead = excelRead;
    }

    @RequestMapping("/")
    public @ResponseBody
    List<Player> greeting() throws IOException, InterruptedException {
        return rawDataService.getData();
    }


}

package cz.simt.tabule.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;

import cz.simt.tabule.data.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Route;
import cz.simt.tabule.repository.RouteRepository;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final ApiRead apiRead;
    private final TimesService timesService;
    private static final Logger logger = LoggerFactory.getLogger("RouteService");

    @Autowired
    public RouteService(RouteRepository routesRepository, ApiRead apiRead, TimesService timesService) {
        this.routeRepository = routesRepository;
        this.apiRead = apiRead;
        this.timesService = timesService;
    }

    @PostConstruct
    public void routes() {
        logger.info("Loading routes started..");
        String[] splitFullRoute;
        try {
            splitFullRoute = apiRead.readFromUrl("https://simt.cz/server/dispData.php?kod=oohq1d8");
        } catch (IOException e) {
            logger.error("CANNOT LOAD ROUTES, SKIPPING..\n" + e.getMessage());
            return;
        }

        for (int i = 0; i < splitFullRoute.length - 1; i++) {
            int soucetSpicka = 0; int soucetSedlo = 0; int soucetNoc = 0;
            String[] splitRoute = splitFullRoute[i].split("/");
            String[] timeSpicka = splitRoute[2].split(":");
            String[] timeSedlo = splitRoute[3].split(":");
            String[] timeNoc = splitRoute[4].split(":");
            String[] stops = splitRoute[5].split(":");

            routeRepository.save(new Route(splitRoute[0], splitRoute[1], 0, stops[0], soucetSpicka, soucetSedlo, soucetNoc));

            for (int j = 1; j<stops.length; j++) {
                soucetSpicka += Integer.parseInt(timeSpicka[j-1]);
                soucetSedlo += Integer.parseInt(timeSedlo[j-1]);
                soucetNoc += Integer.parseInt(timeNoc[j-1]);
                routeRepository.save(new Route(splitRoute[0], splitRoute[1], j, stops[j], soucetSpicka, soucetSedlo, soucetNoc));
            }
        }
        timesService.saveTime(new Times("routesApiGenerated", LocalDateTime.parse(splitFullRoute[splitFullRoute.length-1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        timesService.saveCurrentTime("routesLoaded");
        logger.info("Loading routes DONE.");
    }

    public List<Route> getRoute(String line, String direction) {
       return routeRepository.findRoute(line, direction);
    }

    public List<Route> getRoutesWithoutDepot() {
        return routeRepository.findAllRoutesWithoutDepot();
    }
}

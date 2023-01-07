package cz.simt.tabule.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import cz.simt.tabule.data.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.apiurl.route}")
    private String apiUrl;

    public void routes() {
        logger.info("Loading routes started..");
        String[] splitFullRoute = null;
        while (splitFullRoute == null) {
            try {
                splitFullRoute = apiRead.readFromUrl(apiUrl);
            } catch (IOException e) {
                logger.error("CANNOT LOAD ROUTES, WAITING 10SEC TO ANOTHER TRY..\n" + e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        routeRepository.deleteAll();
        //22/A/1:2:1:1:2/1:2:1:1:2/1:2:1:1:2/104:193:114:116:63:169/0
        //Linka/směr/casySpička/časySedlo/časyNoc/zastávky/trakce - 0=autobus, 1=trolejbus, 2=tramvaj, 3=vlak
        for (int i = 0; i < splitFullRoute.length - 1; i++) {
            int soucetSpicka = 0; int soucetSedlo = 0; int soucetNoc = 0;
            String[] splitRoute = splitFullRoute[i].split("/");
            String[] timeSpicka = splitRoute[2].split(":");
            String[] timeSedlo = splitRoute[3].split(":");
            String[] timeNoc = splitRoute[4].split(":");
            String[] stops = splitRoute[5].split(":");
            int traction =  Integer.parseInt(splitRoute[6]);

            routeRepository.save(new Route(splitRoute[0], splitRoute[1], 0, stops[0], soucetSpicka, soucetSedlo, soucetNoc, traction));

            for (int j = 1; j<stops.length; j++) {
                soucetSpicka += Integer.parseInt(timeSpicka[j-1]);
                soucetSedlo += Integer.parseInt(timeSedlo[j-1]);
                soucetNoc += Integer.parseInt(timeNoc[j-1]);
                routeRepository.save(new Route(splitRoute[0], splitRoute[1], j, stops[j], soucetSpicka, soucetSedlo, soucetNoc, traction));
            }
        }
        timesService.saveTime(new Times("routesJsonGenerated", LocalDateTime.parse(splitFullRoute[splitFullRoute.length-1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        timesService.saveCurrentTime("routesLoaded");
        logger.info("Loading routes DONE.");
    }

    public List<Route> getRoute(String line, String direction) {
       return routeRepository.findRoute(line, direction);
    }

    public List<Route> getRoutesWithoutDepot() {
        return routeRepository.findAllRoutesWithoutDepot();
    }

    public boolean lineExist(String line) {
        return routeRepository.countRoute(line) > 0;
    }

    //Manipulační jízda
    public String getHandlingType(String route) {
        String endStation;
        switch (route) {
            case "A":
                return "Manipulační jízda";
            case "B":
                return "Vůz jede do vozovny";
            case "C":
                return "Mimo provoz";
            case "D":
                return "Cvičná jízda";
            case "E":
                return "Objednaná jízda";
            case "F":
                return "Přestávka - Pause";
            case "G":
                return "Služební jízda";
            case "H":
                return "Pozor vadný vůz";
            default:
                return "";
        }
    }

    public List<String> getAllLines() {
        return routeRepository.getAllLines();
    }

    public int getLineTraction(String line) {
        Optional<Integer> traction = routeRepository.getLineTraction(line);
        return traction.orElse(-1);
    }

    public List<String> getDirectionsForLine(String line) {
        return routeRepository.getDirectionForLine(line);
    }
}

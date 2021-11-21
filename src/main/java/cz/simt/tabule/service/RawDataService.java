package cz.simt.tabule.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.simt.tabule.data.Player;
import cz.simt.tabule.repository.PlayersRepository;

@Service
public class RawDataService {
    private final ExcelRead excelRead;


    @Autowired

    public RawDataService(ExcelRead excelRead) {
        this.excelRead = excelRead;
    }

    public List<Player> getData() {
      //  excelRead.getRoute("1-A");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://simt-mhd.net/app/mapa/gen.php"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String[] split = response.body().split("1@");
        List<Player> players = new ArrayList<>();

        for (String playerRaw : split) {
            if (playerRaw.length() != 0) {
            //    players.add(createPlayer(playerRaw));
            }
        }
        return players;
    }
/*
    private Player createPlayer(String playerRaw) {
        Player player = new Player();

        playerRaw = playerRaw.replace("\n", "").replace("\r", "");
        Pattern p = Pattern.compile("data-id=\"(.*?)\" |data-linka-trasa=\"(.*?)\" |<span class=\"nick\">(.*?)</span>|<span class=\"cas\">(.*?)</span>");
        Matcher m = p.matcher(playerRaw);


        if (m.find()) {
            player.setId(Long.parseLong(m.group(1)));
        }
        if (m.find()) {
            player.setRoute(m.group(2));
        }
        if (m.find()) {
            player.setNick(m.group(3));
        }
        if (m.find()) {
            player.setTime(timeFromDelay(m.group(4)));
        }


        return player;
    }

 */

    private LocalTime timeFromDelay(String delay) {
        LocalTime time = LocalTime.now();
        Pattern delayWithMinutes = Pattern.compile("zpoždění (.*?)m (.*?)s");
        Pattern delayWithoutMinutes = Pattern.compile("zpoždění -(.*?)s");
        Pattern aheadWithMinutes = Pattern.compile("čeká (.*?)m (.*?)s");
        Pattern aheadWithoutMinutes = Pattern.compile("čeká (.*?)s");

        if (delay.matches("zpoždění .*m .*s")) {
            Matcher m = delayWithMinutes.matcher(delay);
            if (m.find()) {
                time = time.minusMinutes(Integer.parseInt(m.group(1)));
                time = time.minusSeconds(Integer.parseInt(m.group(2)));
            }
            return time.truncatedTo(ChronoUnit.MINUTES);

        } else if(delay.matches("zpoždění -.*s")) {
            Matcher m = delayWithoutMinutes.matcher(delay);
            if (m.find()) time = time.minusSeconds(Integer.parseInt(m.group(1)));
            return time.truncatedTo(ChronoUnit.MINUTES);

        } else if(delay.matches("čeká .*m .*s")) {
            Matcher m = aheadWithMinutes.matcher(delay);
            if (m.find()) {
                time = time.plusMinutes(Integer.parseInt(m.group(1)));
                time = time.plusSeconds(Integer.parseInt(m.group(2)));
            }
            return time.truncatedTo(ChronoUnit.MINUTES);

        } else if (delay.matches("čeká .*s")) {
            Matcher m = aheadWithoutMinutes.matcher(delay);
            if (m.find()) time = time.plusSeconds(Integer.parseInt(m.group(1)));
            return time.truncatedTo(ChronoUnit.MINUTES);

        } else return null;
    }
}

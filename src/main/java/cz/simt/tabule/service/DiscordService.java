package cz.simt.tabule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Service
@PropertySource("discord-settings.properties")
public class DiscordService {
    @Value("${discord.client.id}")
    private String clientId;

    @Value("${discord.client.secret}")
    private String clientSecret;

    private static final Logger logger = LoggerFactory.getLogger("DiscordService");

    public String authenticate(String code){
        String requestBody = "client_id="+clientId+"&client_secret="+clientSecret+"&code="+code+"&grant_type=authorization_code&redirect_uri=http://localhost:8081/simt-tabule-vue/dispecer";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://discord.com/api/oauth2/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response.body());
        return response.body();
    }


}

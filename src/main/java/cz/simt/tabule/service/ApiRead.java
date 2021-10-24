package cz.simt.tabule.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariConfig;

@Service
public class ApiRead {

    public String[] readFromUrl(String url) throws IOException {
        System.out.println("Client, request");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        System.out.println("Response");
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
        System.out.println("Decode");
        assert response != null;
        byte[] decodedBytes = Base64.getMimeDecoder().decode(response.body());
        String decodedResponse = new String(decodedBytes);
        System.out.println("Return");
        return decodedResponse.split("\\|");
    }
}

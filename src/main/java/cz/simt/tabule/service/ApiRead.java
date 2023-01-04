package cz.simt.tabule.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApiRead {
    private static final Logger logger = LoggerFactory.getLogger("ApiRead");

    public String[] readFromUrl(String url) throws IOException {
        logger.debug("Client, request");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        logger.debug("Response");
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
        logger.debug("Decode");
        assert response != null;
        byte[] decodedBytes = Base64.getMimeDecoder().decode(response.body());
        String decodedResponse = new String(decodedBytes);
        decodedResponse = decodedResponse.replaceAll("\\p{C}", "");
        logger.debug("Return");
        return decodedResponse.split("\\|");
    }
}

package app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import app.dto.AddressDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AddressApiService {

    private final ObjectMapper mapper;
    private final HttpClient client;

    public AddressApiService() {
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
    }

    public boolean isValidPostalCode(String postnr, String by) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.dataforsyningen.dk/postnumre/" + postnr))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return false;
        }

        AddressDTO dto = mapper.readValue(response.body(), AddressDTO.class);
        return dto.navn().equalsIgnoreCase(by);
    }
}
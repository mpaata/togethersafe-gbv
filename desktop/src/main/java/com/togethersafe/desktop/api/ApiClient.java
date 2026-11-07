package com.togethersafe.desktop.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.togethersafe.desktop.model.CaseItem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readTree(response.body());
    }

    public static JsonNode post(String path, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readTree(response.body());
    }

    public static List<CaseItem> fetchCases() throws IOException, InterruptedException {
        JsonNode root = get("/cases");
        List<CaseItem> cases = new ArrayList<>();

        if (root.has("data") && root.get("data").isArray()) {
            for (JsonNode node : root.get("data")) {
                cases.add(new CaseItem(
                    node.get("id").asLong(),
                    node.get("caseReference").asText(),
                    node.get("caseType").asText(),
                    node.get("description").asText(),
                    node.get("status").asText(),
                    node.get("priority").asText(),
                    node.get("createdAt").asText()
                ));
            }
        }

        return cases;
    }
}
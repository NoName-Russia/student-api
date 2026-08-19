package com.electrostore.studentapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${2gis.api-key}")
    private String apiKey;

    public UniversityService() {

        this.restClient = RestClient.builder()
                .baseUrl("https://catalog.api.2gis.com")
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public List<UniversityResponse> searchUniversities(
            String query
    ) {

        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        String searchQuery =
                query.trim() + " вуз университет институт";

        try {

            String response = restClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/3.0/items")
                                    .queryParam("q", searchQuery)
                                    .queryParam("type", "branch")
                                    .queryParam("page_size", 10)
                                    .queryParam("page", 1)
                                    .queryParam("locale", "ru_RU")
                                    .queryParam("key", apiKey)
                                    .build()
                    )
                    .retrieve()
                    .body(String.class);

            return parseResponse(response);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Ошибка при обращении к 2ГИС: "
                            + e.getMessage()
            );
        }
    }

    private List<UniversityResponse> parseResponse(
            String json
    ) throws Exception {

        List<UniversityResponse> universities =
                new ArrayList<>();

        JsonNode root =
                objectMapper.readTree(json);

        JsonNode items =
                root.path("result").path("items");

        if (!items.isArray()) {
            return universities;
        }

        for (JsonNode item : items) {

            String id =
                    item.path("id").asText("");

            String name =
                    item.path("name").asText("");

            String address =
                    item.path("address_name").asText("");

            String fullName =
                    item.path("full_name").asText("");

            if (!name.isBlank()) {

                universities.add(
                        new UniversityResponse(
                                id,
                                name,
                                address,
                                fullName
                        )
                );
            }
        }

        return universities;
    }

    public record UniversityResponse(
            String id,
            String name,
            String address,
            String fullName
    ) {
    }
}
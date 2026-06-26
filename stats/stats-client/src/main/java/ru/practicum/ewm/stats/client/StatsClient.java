package ru.practicum.ewm.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.HitCreateDto;
import ru.practicum.ewm.stats.dto.StatsResponseDto;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class StatsClient {

    private final String baseUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public StatsClient(@Value("${services.stats-service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void postHit(HitCreateDto hitData) throws IOException, InterruptedException {
        String url = baseUrl + "/hit";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(hitData)))
                .build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() != 201) {
            log.warn(
                    "StatsClient: код ответа сервера статистики отличается от 201 (statusCode = {})",
                    response.statusCode()
            );
        }
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end)
            throws IOException, InterruptedException {
        return getStats(start, end, null, null);
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris)
            throws IOException, InterruptedException {
        return getStats(start, end, uris, null);
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique)
            throws IOException, InterruptedException {
        return getStats(start, end, null, unique);
    }

    public List<StatsResponseDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris,
            Boolean unique
    ) throws IOException, InterruptedException {

        StringBuilder urlBuilder = new StringBuilder(baseUrl + "/stats?");
        urlBuilder.append("start=").append(URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8));
        urlBuilder.append("&end=").append(URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8));

        if (uris != null && !uris.isEmpty()) {
            uris.forEach(uri -> urlBuilder.append("&uris=").append(URLEncoder.encode(uri, StandardCharsets.UTF_8)));
        }

        if (unique != null) {
            urlBuilder.append("&unique=").append(unique);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            log.warn(
                    "StatsClient: код ответа сервера статистики отличается от 200 (statusCode = {})",
                    response.statusCode()
            );
            return List.of();
        }

    }
}

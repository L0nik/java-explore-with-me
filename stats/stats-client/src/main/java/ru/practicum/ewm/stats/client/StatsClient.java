package ru.practicum.ewm.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class StatsClient {

    private static final String baseUrl = "http://localhost:9090";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end)
            throws IOException, InterruptedException {
        return getStats(start, end, null, null);
    }

    public static List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris)
            throws IOException, InterruptedException {
        return getStats(start, end, uris, null);
    }

    public static List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique)
            throws IOException, InterruptedException {
        return getStats(start, end, null, unique);
    }

    public static List<StatsResponseDto> getStats(
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

        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<>() {});
        } else {
            log.info(
                    "StatsClient: код ответа сервера статистики отличается от 200 (statusCode = {})",
                    response.statusCode()
            );
            return List.of();
        }

    }
}

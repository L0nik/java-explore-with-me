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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient {

    private final String baseUrl;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final static LocalDateTime MIN_VIEWS_DATE =
            LocalDateTime.of(2000, 1, 1, 0, 0, 0);

    public StatsClient(@Value("${services.stats-service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void postHit(HitCreateDto hitData){
        String url = baseUrl + "/hit";
        try {
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
        } catch (IOException e) {
            log.error("Ошибка при обращении к сервису статистики", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос к сервису статистики был прерван", e);
        }
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end) {
        return getStats(start, end, null, null);
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris) {
        return getStats(start, end, uris, null);
    }

    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return getStats(start, end, null, unique);
    }

    public List<StatsResponseDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris,
            Boolean unique
    ) {

        StringBuilder urlBuilder = new StringBuilder(baseUrl + "/stats?");
        urlBuilder.append("start=").append(URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8));
        urlBuilder.append("&end=").append(URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8));

        if (uris != null && !uris.isEmpty()) {
            uris.forEach(uri -> urlBuilder.append("&uris=").append(URLEncoder.encode(uri, StandardCharsets.UTF_8)));
        }

        if (unique != null) {
            urlBuilder.append("&unique=").append(unique);
        }

        try {

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
            }
        } catch (IOException e) {
            log.error("Ошибка при обращении к сервису статистики", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Запрос к сервису статистики был прерван", e);
        }

        return List.of();

    }

    public Map<String, Integer> getViews(Collection<String> uris) {
        List<StatsResponseDto> stats =  getStats(MIN_VIEWS_DATE, LocalDateTime.now(), uris);
        Map<String, Integer> views = new HashMap<>();
        stats.forEach(statsResponseDto ->
                views.put(
                        statsResponseDto.getUri(),
                        statsResponseDto.getHits() + views.getOrDefault(statsResponseDto.getUri(), 0)
                )
        );
        return views;
    }
}

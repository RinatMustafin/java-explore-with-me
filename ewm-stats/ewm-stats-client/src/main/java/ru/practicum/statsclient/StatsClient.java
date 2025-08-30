package ru.practicum.statsclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.EndpointHit;
import ru.practicum.stats.ViewStats;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatsClient {

    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(String baseUrl) {

        this.baseUrl = baseUrl != null && baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
    }

    public void saveHit(EndpointHit hit) {
        try {
            rest.postForEntity(baseUrl + "/hit", hit, Void.class);
        } catch (Exception ignored) {

        }
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        try {
            UriComponentsBuilder b = UriComponentsBuilder
                    .fromHttpUrl(baseUrl)
                    .path("/stats")
                    .queryParam("start", start.format(FMT))
                    .queryParam("end", end.format(FMT))
                    .queryParam("unique", unique);

            if (uris != null && !uris.isEmpty()) {
                for (String u : uris) {
                    b.queryParam("uris", u);
                }
            }

            URI uri = b.encode().build().toUri();
            ResponseEntity<ViewStats[]> resp = rest.getForEntity(uri, ViewStats[].class);
            ViewStats[] body = resp.getBody();
            return (body == null) ? Collections.emptyList() : Arrays.asList(body);
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

}
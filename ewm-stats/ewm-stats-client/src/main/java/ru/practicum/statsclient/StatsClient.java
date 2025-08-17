package ru.practicum.statsclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.EndpointHit;
import ru.practicum.stats.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            StringBuilder url = new StringBuilder(baseUrl).append("/stats?")
                    .append("start=").append(enc(start.format(FMT)))
                    .append("&end=").append(enc(end.format(FMT)))
                    .append("&unique=").append(unique);

            if (uris != null) {
                for (String u : uris) {
                    url.append("&uris=").append(enc(u));
                }
            }

            ResponseEntity<ViewStats[]> resp = rest.getForEntity(url.toString(), ViewStats[].class);
            ViewStats[] body = resp.getBody();
            return (body == null) ? Collections.emptyList() : Arrays.asList(body);

        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
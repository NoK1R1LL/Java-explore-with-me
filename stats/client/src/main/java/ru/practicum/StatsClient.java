package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.StatsDtoForSave;
import ru.practicum.dto.StatsDtoForView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsClient {
    private final RestTemplate restTemplate;
    @Value("${statsServerUrl}")
    private String statsServer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseEntity<List<StatsDtoForView>> getStats(LocalDateTime start, LocalDateTime end,
                                                          String[] uris, boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        StringBuilder urisForExchange = new StringBuilder();
        for (int i = 0; i < uris.length; i++) {
            if (i < (uris.length - 1)) {
                urisForExchange.append("uris").append("=").append(uris[i]).append(",");
            } else {
                urisForExchange.append("uris").append("=").append(uris[i]);
            }
        }
        Map<String, Object> uriVariables = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisForExchange.toString(),
                "unique", unique);

        String uri = statsServer + "/stats?start={start}&end={end}&{uris}&{unique}";
        log.info("** GET STATS: **\t\t{}", uri);
        ParameterizedTypeReference<List<StatsDtoForView>> parTypeRef =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<List<StatsDtoForView>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
                parTypeRef, uriVariables);
        log.info(response.toString());
        return response;
    }

    public void save(StatsDtoForSave statsDtoForSave) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoForSave> requestEntity = new HttpEntity<>(statsDtoForSave, httpHeaders);
        restTemplate.exchange(statsServer + "hit", HttpMethod.POST, requestEntity, StatsDtoForSave.class);
    }
}

package com.datapath.analyticapp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class RestManager {

    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final RestTemplate restTemplate;

    public String getUrlByOffset(String apiUrl, LocalDateTime offset, int limit) {
        return new DefaultUriBuilderFactory(apiUrl).builder()
                .queryParam(LIMIT, limit)
                .queryParam(OFFSET, offset)
                .build().toString();
    }

    public <T> T getData(String url, Class<T> t) {
        log.info("Export {} from {}", t.getSimpleName(), url);
        return restTemplate.getForObject(url, t);
    }
}

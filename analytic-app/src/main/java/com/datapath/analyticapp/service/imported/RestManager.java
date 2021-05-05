package com.datapath.analyticapp.service.imported;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RestManager {


    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final String exportApiUrl;
    private final RestTemplate restTemplate;

    public RestManager(@Value("${checklist.export.url}") String exportApiUrl, RestTemplate restTemplate) {
        this.exportApiUrl = exportApiUrl;
        this.restTemplate = restTemplate;
    }

    public String getUrlByOffset(String apiUrlPart, LocalDateTime offset, int limit) {
        return new DefaultUriBuilderFactory(exportApiUrl + apiUrlPart).builder()
                .queryParam(LIMIT, limit)
                .queryParam(OFFSET, offset)
                .build().toString();
    }

    public String getUrlById(String apiUrlPart, Long id) {
        return new DefaultUriBuilderFactory(exportApiUrl + apiUrlPart).builder()
                .pathSegment("{id}")
                .build(id).toString();
    }

    public <T> T getData(String url, Long id, Class<T> t) {
        log.info("GET {} {}", t.getSimpleName(), url);
        return restTemplate.getForObject(url, t, id);
    }

    public <T> T getData(String url, Class<T> t) {
        log.info("GET {} {}", t.getSimpleName(), url);
        return restTemplate.getForObject(url, t);
    }
}

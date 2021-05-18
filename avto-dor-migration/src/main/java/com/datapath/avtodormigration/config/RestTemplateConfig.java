package com.datapath.avtodormigration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestTemplateConfig {

    @Value("${checklist.api.url}")
    private String rootUrl;
    @Value("${checklist.auth.token}")
    private String token;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .rootUri(rootUrl)
                .defaultHeader("Authorization", token)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

package com.datapath.analyticapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableNeo4jAuditing
@EnableScheduling
public class AnalyticAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticAppApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }

    @Bean
    public RestTemplate restTemplate() {
        DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory();
        builderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return new RestTemplateBuilder()
                .uriTemplateHandler(builderFactory)
                .setConnectTimeout(Duration.ofMinutes(1))
                .setReadTimeout(Duration.ofMinutes(1))
                .build();
    }
}

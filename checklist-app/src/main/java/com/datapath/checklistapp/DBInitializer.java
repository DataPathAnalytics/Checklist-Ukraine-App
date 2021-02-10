package com.datapath.checklistapp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@Slf4j
@Component
@AllArgsConstructor
public class DBInitializer implements InitializingBean {

    private final Neo4jClient client;
    private final ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() {
        log.info("DB initialization started");
        runLineQueryFromFile("schema");
        runLineQueryFromFile("data");
        runQueriesFromFile("answerStructures");
        runLineQueryFromFile("relationship");
        log.info("DB initialization finished");
    }

    private void runQueriesFromFile(String fileName) {
        try {
            String filePath = String.format("classpath:db/%s.txt", fileName);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(resourceLoader.getResource(filePath).getInputStream(), UTF_8));

            List<String> queries = Arrays.asList(br.lines()
                    .filter(Strings::isNotEmpty)
                    .collect(joining())
                    .split(";"));

            queries.forEach(l -> {
                try {
                    client.query(l).run();
                } catch (DataIntegrityViolationException e) {
                    log.warn(e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("DB initialization failed in file " + fileName, e);
        }
    }

    private void runLineQueryFromFile(String fileName) {
        try {
            String filePath = String.format("classpath:db/%s.txt", fileName);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(resourceLoader.getResource(filePath).getInputStream(), UTF_8));

            br.lines()
                    .filter(Strings::isNotEmpty)
                    .forEach(l -> {
                        try {
                            client.query(l).run();
                        } catch (DataIntegrityViolationException e) {
                            log.warn(e.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("DB initialization failed in file " + fileName, e);
        }
    }
}

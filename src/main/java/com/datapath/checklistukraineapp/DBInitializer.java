package com.datapath.checklistukraineapp;

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

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@AllArgsConstructor
public class DBInitializer implements InitializingBean {

    private final Neo4jClient client;
    private final ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() {
        log.info("DB initialization started");
        runQueriesFromFile("schema");
        runQueriesFromFile("data");
        runQueriesFromFile("relationship");
        log.info("DB initialization finished");
    }

    private void runQueriesFromFile(String fileName) {
        try {
            String filePath = String.format("classpath:db/%s.txt", fileName);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(resourceLoader.getResource(filePath).getInputStream(), UTF_8));

            br.lines()
                    .filter(Strings::isNotEmpty)
                    .forEach(l -> client.query(l).run());
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
        } catch (Exception e) {
            log.error("DB initialization failed", e);
        }
    }
}

package com.datapath.avtodormigration.service;

import com.datapath.avtodormigration.domain.MigrationHistory;
import com.datapath.avtodormigration.domain.MigrationState;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class MigrationHistoryService {

    private static final String MIGRATION_HISTORY_FILE = "migration_history.json";

    private ObjectMapper mapper;

    private static Map<String, Integer> buyerControlActivities;
    private static Map<String, Integer> contractResponseSessions;

    @PostConstruct
    private void init() throws IOException {
        Path path = Paths.get(MIGRATION_HISTORY_FILE);

        if (Files.exists(path)) {
            MigrationHistory migrationHistory = mapper.readValue(path.toFile(), MigrationHistory.class);

            buyerControlActivities = migrationHistory.getBuyerControlActivities()
                    .stream()
                    .collect(toMap(MigrationState::getIdentifier, MigrationState::getId));

            contractResponseSessions = migrationHistory.getContractResponseSessions()
                    .stream()
                    .collect(toMap(MigrationState::getIdentifier, MigrationState::getId));
        } else {
            buyerControlActivities = new HashMap<>();
            contractResponseSessions = new HashMap<>();
        }
    }

    @PreDestroy
    private void destroy() throws IOException {
        MigrationHistory history = new MigrationHistory();
        history.setBuyerControlActivities(
                buyerControlActivities.entrySet().stream()
                        .map(e -> new MigrationState(e.getKey(), e.getValue()))
                        .collect(toList())
        );
        history.setContractResponseSessions(
                contractResponseSessions.entrySet().stream()
                        .map(e -> new MigrationState(e.getKey(), e.getValue()))
                        .collect(toList())
        );

        mapper.writeValue(Paths.get(MIGRATION_HISTORY_FILE).toFile(), history);
    }

    public Integer getControlActivityIdByBuyerIdentifier(String identifier) {
        return buyerControlActivities.getOrDefault(identifier, null);
    }

    public boolean existsByContractIdentifier(String identifier) {
        return contractResponseSessions.containsKey(identifier);
    }

    public void addBuyerControlActivity(String identifier, Integer id) {
        buyerControlActivities.put(identifier, id);
    }

    public void addContractResponseSession(String identifier, Integer id) {
        contractResponseSessions.put(identifier, id);
    }
}

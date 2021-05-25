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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class MigrationHistoryService {

    private static final String MIGRATION_HISTORY_FILE = "migration_history.json";

    private ObjectMapper mapper;

    private static Map<Integer, MigrationState> historyMap;

    @PostConstruct
    private void init() throws IOException {
        Path path = Paths.get(MIGRATION_HISTORY_FILE);

        if (Files.exists(path)) {
            MigrationHistory migrationHistory = mapper.readValue(path.toFile(), MigrationHistory.class);

            historyMap = migrationHistory.getHistory()
                    .stream()
                    .collect(toMap(MigrationState::getChecklistId, Function.identity()));
        } else {
            historyMap = new HashMap<>();
        }
    }

    @PreDestroy
    private void destroy() throws IOException {
        MigrationHistory history = new MigrationHistory();
        history.setHistory(new ArrayList<>(historyMap.values()));

        mapper.writeValue(Paths.get(MIGRATION_HISTORY_FILE).toFile(), history);
    }

    public MigrationState getMigrationStateByChecklistId(Integer id) {
        return historyMap.get(id);
    }

    public void addMigrationState(MigrationState state) {
        historyMap.put(state.getChecklistId(), state);
    }
}

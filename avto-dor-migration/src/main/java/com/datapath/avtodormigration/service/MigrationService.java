package com.datapath.avtodormigration.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class MigrationService {

    private final MigrationHistoryService historyService;
    private final RestTemplate restTemplate;

    public void doMigrate() {

    }
}

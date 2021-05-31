package com.datapath.analyticapp.service.imported;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportScheduler {

    private final boolean isImportEnable;
    private final List<ImportService> importServices;

    public ImportScheduler(@Value("${checklist.data.import.enable}") boolean isImportEnable,
                           List<ImportService> importServices) {
        this.importServices = importServices;
        this.isImportEnable = isImportEnable;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void update() {
        if (isImportEnable)
            importServices.forEach(ImportService::upload);
    }
}

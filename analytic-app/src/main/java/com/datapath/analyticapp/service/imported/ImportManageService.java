package com.datapath.analyticapp.service.imported;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ImportManageService {

    private final List<ImportService> importServices;

    @Scheduled(fixedDelay = 1000 * 60)
    public void update() {
        importServices.forEach(ImportService::upload);
    }
}

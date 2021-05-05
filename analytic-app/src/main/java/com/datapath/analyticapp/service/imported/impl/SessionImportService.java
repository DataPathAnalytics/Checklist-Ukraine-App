package com.datapath.analyticapp.service.imported.impl;

import com.datapath.analyticapp.dto.imported.response.ChecklistDateResponse;
import com.datapath.analyticapp.dto.imported.response.ControlActivitySessionDTO;
import com.datapath.analyticapp.service.imported.ImportService;
import com.datapath.analyticapp.service.imported.RestManager;
import com.datapath.analyticapp.service.miner.handler.SessionUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Order(4)
@Slf4j
public class SessionImportService implements ImportService {

    private static final int LIMIT = 10;

    @Value("${checklist.sessions.part.url}")
    private String apiUrlPart;

    @Autowired
    private RestManager restManager;
    @Autowired
    private SessionUpdateHandler updateService;

    @Override
    public void upload() {
        String url = restManager.getUrlByOffset(apiUrlPart, null, LIMIT);

        ChecklistDateResponse response;

        do {
            response = restManager.getData(url, ChecklistDateResponse.class);

            if (isEmpty(response.getData())) break;

            response.getData().forEach(dto -> {
                if (updateService.needToUpdate(dto)) {
                    String sessionUrl = restManager.getUrlById(apiUrlPart, dto.getId());
                    updateService.update(restManager.getData(sessionUrl, dto.getId(), ControlActivitySessionDTO.class));
                }
            });

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), LIMIT);

        } while (true);

        log.info("Updating response sessions completed");
    }
}

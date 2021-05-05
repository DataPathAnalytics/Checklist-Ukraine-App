package com.datapath.analyticapp.service.imported.impl;

import com.datapath.analyticapp.dto.imported.response.ChecklistDateResponse;
import com.datapath.analyticapp.dto.imported.response.ControlActivityDTO;
import com.datapath.analyticapp.service.imported.ImportService;
import com.datapath.analyticapp.service.imported.RestManager;
import com.datapath.analyticapp.service.miner.handler.ActivityUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Order(3)
@Slf4j
public class ActivityImportService implements ImportService {

    private static final int LIMIT = 10;

    @Value("${checklist.activity.part.url}")
    private String apiUrlPart;

    @Autowired
    private RestManager restManager;
    @Autowired
    private ActivityUpdateHandler updateService;

    @Override
    public void upload() {
        String url = restManager.getUrlByOffset(apiUrlPart, null, LIMIT);

        ChecklistDateResponse response;

        do {
            response = restManager.getData(url, ChecklistDateResponse.class);

            if (isEmpty(response.getData())) break;

            response.getData().forEach(dto -> {
                if (updateService.needToUpdate(dto)) {
                    String activityUrl = restManager.getUrlById(apiUrlPart, dto.getId());
                    updateService.update(restManager.getData(activityUrl, dto.getId(), ControlActivityDTO.class));
                }
            });

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), LIMIT);

        } while (true);

        log.info("Updating control activities completed");
    }
}

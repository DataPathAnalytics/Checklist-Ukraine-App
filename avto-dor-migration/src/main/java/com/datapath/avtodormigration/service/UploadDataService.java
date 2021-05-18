package com.datapath.avtodormigration.service;

import com.datapath.avtodormigration.dto.request.ControlActivityRequest;
import com.datapath.avtodormigration.dto.request.ResponseSessionRequest;
import com.datapath.avtodormigration.dto.response.SavedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UploadDataService {

    private final RestTemplate restTemplate;
    private final String controlActivityCreateUrl;
    private final String responseSessionCreateUrl;

    public UploadDataService(RestTemplate restTemplate,
                             @Value("${control.activity.create.url}") String controlActivityCreateUrl,
                             @Value("${response.session.create.url}") String responseSessionCreateUrl) {
        this.restTemplate = restTemplate;
        this.controlActivityCreateUrl = controlActivityCreateUrl;
        this.responseSessionCreateUrl = responseSessionCreateUrl;
    }

    public Integer uploadControlActivity(ControlActivityRequest request) {
        return restTemplate.postForObject(controlActivityCreateUrl, request, SavedResponse.class).getId();
    }

    public Integer uploadResponseSession(ResponseSessionRequest request) {
        return restTemplate.postForObject(responseSessionCreateUrl, request, SavedResponse.class).getId();
    }
}

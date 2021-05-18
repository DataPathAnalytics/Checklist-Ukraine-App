package com.datapath.avtodormigration.service;

import com.datapath.avtodormigration.dto.response.QuestionExecutionDTO;
import com.datapath.avtodormigration.dto.response.TemplateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class TemplateProvideService {

    private final int templateConfigId;
    private final int templateId;
    private final String templateConfigUrl;
    private final String templateUrl;
    private final RestTemplate restTemplate;

    private TemplateDTO templateConfig;
    private TemplateDTO template;

    public TemplateProvideService(@Value("${template.config.id}") int templateConfigId,
                                  @Value("${template.id}") int templateId,
                                  @Value("${template.config.url}") String templateConfigUrl,
                                  @Value("${template.url}") String templateUrl,
                                  RestTemplate restTemplate) {
        this.templateConfigId = templateConfigId;
        this.templateId = templateId;
        this.templateConfigUrl = templateConfigUrl;
        this.templateUrl = templateUrl;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void init() {
        templateConfig = restTemplate.getForObject(templateConfigUrl, TemplateDTO.class, templateConfigId);
        template = restTemplate.getForObject(templateUrl, TemplateDTO.class, templateId);
    }

    public TemplateDTO getTemplateConfig() {
        return templateConfig;
    }

    public TemplateDTO getTemplate() {
        return template;
    }

    public int getTemplateConfigId() {
        return templateConfigId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public Integer extractQuestionIdByValue(List<QuestionExecutionDTO> executions, String value) {
        return executions.stream()
                .filter(ex -> value.equals(ex.getQuestion().getValue()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("not found question by value"))
                .getId();
    }
}

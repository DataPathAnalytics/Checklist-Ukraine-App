package com.datapath.dasuchecklistmigration.service;

import com.datapath.dasuchecklistmigration.dto.response.QuestionExecutionDTO;
import com.datapath.dasuchecklistmigration.dto.response.TemplateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.datapath.dasuchecklistmigration.Constants.COVID_19_AVTODOR_TEMPLATE_TYPE_ID;
import static com.datapath.dasuchecklistmigration.Constants.COVID_19_TEMPLATE_TYPE_ID;

@Slf4j
@Service
public class TemplateProvideService {

    private final int templateConfigId;
    private final int templateId;
    private final int avtodorTemplateConfigId;
    private final int avtodorTemplateId;
    private final String templateConfigUrl;
    private final String templateUrl;
    private final RestTemplate restTemplate;
    private final AuthService authService;

    private TemplateDTO templateConfig;
    private TemplateDTO template;
    private TemplateDTO avtodorTemplateConfig;
    private TemplateDTO avtodorTemplate;

    public TemplateProvideService(@Value("${template.config.id}") int templateConfigId,
                                  @Value("${template.id}") int templateId,
                                  @Value("${avtodor.template.config.id}") int avtodorTemplateConfigId,
                                  @Value("${avtodor.template.id}") int avtodorTemplateId,
                                  @Value("${template.config.url}") String templateConfigUrl,
                                  @Value("${template.url}") String templateUrl,
                                  RestTemplate restTemplate,
                                  AuthService authService) {
        this.templateConfigId = templateConfigId;
        this.templateId = templateId;
        this.avtodorTemplateConfigId = avtodorTemplateConfigId;
        this.avtodorTemplateId = avtodorTemplateId;
        this.templateConfigUrl = templateConfigUrl;
        this.templateUrl = templateUrl;
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @PostConstruct
    private void init() {
        authService.login();

        templateConfig = restTemplate.getForObject(templateConfigUrl, TemplateDTO.class, templateConfigId);
        template = restTemplate.getForObject(templateUrl, TemplateDTO.class, templateId);
        avtodorTemplateConfig = restTemplate.getForObject(templateConfigUrl, TemplateDTO.class, avtodorTemplateConfigId);
        avtodorTemplate = restTemplate.getForObject(templateUrl, TemplateDTO.class, avtodorTemplateId);
    }

    public TemplateDTO getTemplateConfig(Integer templateTypeId) {
        if (COVID_19_TEMPLATE_TYPE_ID.equals(templateTypeId)) return templateConfig;
        if (COVID_19_AVTODOR_TEMPLATE_TYPE_ID.equals(templateTypeId)) return avtodorTemplateConfig;
        throw new RuntimeException("Not found template config");
    }

    public TemplateDTO getTemplate(Integer templateTypeId) {
        if (COVID_19_TEMPLATE_TYPE_ID.equals(templateTypeId)) return template;
        if (COVID_19_AVTODOR_TEMPLATE_TYPE_ID.equals(templateTypeId)) return avtodorTemplate;
        throw new RuntimeException("Not found template config");
    }

    public int getTemplateConfigId(Integer templateTypeId) {
        if (COVID_19_TEMPLATE_TYPE_ID.equals(templateTypeId)) return templateConfigId;
        if (COVID_19_AVTODOR_TEMPLATE_TYPE_ID.equals(templateTypeId)) return avtodorTemplateConfigId;
        throw new RuntimeException("Not found template config id");
    }

    public Integer getTemplateId(Integer templateTypeId) {
        if (COVID_19_TEMPLATE_TYPE_ID.equals(templateTypeId)) return templateId;
        if (COVID_19_AVTODOR_TEMPLATE_TYPE_ID.equals(templateTypeId)) return avtodorTemplateId;
        throw new RuntimeException("Not found template id");
    }

    public Integer extractQuestionIdByValue(List<QuestionExecutionDTO> executions, String value) {
        return executions.stream()
                .filter(ex -> value.equals(ex.getQuestion().getValue()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("not found question by value: " + value))
                .getId();
    }

    public QuestionExecutionDTO extractQuestionById(Integer id, Integer templateTypeId) {
        if (COVID_19_TEMPLATE_TYPE_ID.equals(templateTypeId))
            return template.getQuestionGroups().stream()
                    .flatMap(group -> group.getQuestions().stream())
                    .filter(ex -> id.equals(ex.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("not found question by id: " + id));

        if (COVID_19_AVTODOR_TEMPLATE_TYPE_ID.equals(templateTypeId))
            return avtodorTemplate.getQuestionGroups().stream()
                    .flatMap(group -> group.getQuestions().stream())
                    .filter(ex -> id.equals(ex.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("not found question by id: " + id));

        throw new RuntimeException("Unsupported template type");
    }
}

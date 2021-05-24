package com.datapath.avtodormigration.service.builder.impl;

import com.datapath.avtodormigration.service.TemplateProvideService;
import com.datapath.avtodormigration.service.builder.ControlActivityBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.datapath.avtodormigration.Constants.COVID_19_TEMPLATE_TYPE_ID;

@Service
@AllArgsConstructor
public class Covid19ControlActivityBuilder implements ControlActivityBuilder {

    private final TemplateProvideService templateService;

    @Override
    public Integer forTemplateType() {
        return COVID_19_TEMPLATE_TYPE_ID;
    }

    @Override
    public TemplateProvideService getTemplateProvideService() {
        return templateService;
    }
}

package com.datapath.avtodormigration.service.builder;

import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BuilderProvider {

    private final List<ControlActivityBuilder> controlActivityBuilders;
    private final List<ResponseSessionBuilder> responseSessionBuilders;

    public ControlActivityBuilder getControlActivityBuilder(ChecklistEntity checklistEntity) {
        return controlActivityBuilders.stream()
                .filter(b -> checklistEntity.getTemplateTypeId().equals(b.forTemplateType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found control activity builder"));
    }

    public ResponseSessionBuilder getResponseSessionBuilder(ChecklistEntity checklistEntity) {
        return responseSessionBuilders.stream()
                .filter(b -> checklistEntity.getTemplateTypeId().equals(b.forTemplateType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found response session builder"));
    }
}

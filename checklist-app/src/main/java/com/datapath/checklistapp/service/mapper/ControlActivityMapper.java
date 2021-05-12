package com.datapath.checklistapp.service.mapper;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ControlActivityMapper {

    private final SessionMapper sessionMapper;

    public ControlActivityDTO map(ControlActivityEntity activity) {
        ControlActivityDTO dto = new ControlActivityDTO();

        dto.setId(activity.getId());
        dto.setStatusId(activity.getStatus().getId());

        dto.setActivity(sessionMapper.map(activity.getActivityResponse()));

        dto.setTemplates(
                activity.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        return dto;
    }
}

package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE;
import static com.datapath.checklistapp.util.Constants.DEFAULT_RESPONSE_SESSION_COUNT;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ControlActivityConverter {

    private final ResponseSessionConverter responseSessionConverter;

    public ControlActivityDTO map(ControlActivityEntity activity) {
        ControlActivityDTO dto = new ControlActivityDTO();

        dto.setId(activity.getId());
        dto.setStatusId(activity.getStatus().getId());

        dto.setActivity(responseSessionConverter.map(activity.getActivityResponse()));

        dto.setTemplates(
                activity.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        SessionPageDTO sessionPage = new SessionPageDTO();
        sessionPage.setPageSize(DEFAULT_RESPONSE_SESSION_COUNT);
        sessionPage.setCurrentPage(DEFAULT_EVENT_CHECKLIST_PAGE);
        sessionPage.setTotalCount(activity.getSessionResponses().size());
        sessionPage.setTotalPageCount((int) Math.ceil((double) activity.getSessionResponses().size() / DEFAULT_RESPONSE_SESSION_COUNT));
        sessionPage.setSessions(
                activity.getSessionResponses().stream()
                        .limit(DEFAULT_RESPONSE_SESSION_COUNT)
                        .sorted(Comparator.comparingInt(SessionEntity::getNumber))
                        .map(responseSessionConverter::map)
                        .collect(toList())
        );

        dto.setSessions(sessionPage);

        return dto;
    }
}

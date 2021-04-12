package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT;
import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class ControlActivityConverter {

    private final ResponseSessionConverter responseSessionConverter;
    private final TemplateConverter templateConverter;
    private final AnswerConverter answerConverter;

    public ControlActivityDTO map(ControlActivityEntity activity) {
        ControlActivityDTO dto = new ControlActivityDTO();

        BeanUtils.copyProperties(activity, dto);
        dto.setMembers(
                activity.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
        dto.setAuthorId(activity.getAuthor().getId());
        dto.setStatusId(activity.getStatus().getActivityStatusId());

        dto.setTemplateConfig(templateConverter.map(activity.getActivityResponse().getTemplateConfig()));

        dto.setTemplates(
                activity.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        dto.setAnswers(
                activity.getActivityResponse().getAnswers().stream()
                        .map(answerConverter::map)
                        .collect(toList())
        );

        SessionPageDTO sessionPage = new SessionPageDTO();
        sessionPage.setPageSize(DEFAULT_EVENT_CHECKLIST_COUNT);
        sessionPage.setCurrentPage(DEFAULT_EVENT_CHECKLIST_PAGE);
        sessionPage.setTotalCount(activity.getSessionResponses().size());
        sessionPage.setTotalPageCount((int) Math.ceil((double) activity.getSessionResponses().size() / DEFAULT_EVENT_CHECKLIST_COUNT));
        sessionPage.setSessions(
                activity.getSessionResponses().stream()
                        .limit(DEFAULT_EVENT_CHECKLIST_COUNT)
                        .sorted(Comparator.comparing(ResponseSessionEntity::getDateCreated)
                                .thenComparing(ResponseSessionEntity::getName))
                        .map(responseSessionConverter::mapResponseSessionBaseInfo)
                        .collect(toList())
        );

        dto.setSessions(sessionPage);

        return dto;
    }
}

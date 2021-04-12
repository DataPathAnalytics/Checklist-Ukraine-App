package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ResponseSessionConverter {

    private final TemplateConverter templateConverter;
    private final AnswerConverter answerConverter;

    public ResponseSessionDTO mapResponseSessionBaseInfo(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = new ResponseSessionDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateId(entity.getTemplate().getId());
        dto.setTemplateName(entity.getTemplate().getName());
        dto.setSessionStatusId(entity.getStatus().getSessionStatusId());

        if (nonNull(entity.getReviewer())) {
            dto.setReviewerId(entity.getReviewer().getId());
        }

        return dto;
    }

    public ResponseSessionDTO map(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = mapResponseSessionBaseInfo(entity);

        dto.setTemplate(templateConverter.map(entity.getTemplate()));

        dto.setAnswers(
                entity.getAnswers().stream().map(answerConverter::map).collect(toList())
        );

        return dto;
    }
}

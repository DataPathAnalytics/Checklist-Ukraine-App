package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ResponseSessionConverter {

    private final TemplateConverter templateConverter;
    private final AnswerConverter answerConverter;

    public ResponseSessionDTO map(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = new ResponseSessionDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());

        if (nonNull(entity.getTemplateConfig())) {
            dto.setTemplate(templateConverter.map(entity.getTemplateConfig()));

            if (!isEmpty(entity.getMembers())) {
                dto.setMembers(
                        entity.getMembers()
                                .stream()
                                .map(UserEntity::getId)
                                .collect(toList())
                );
            }
        } else {
            dto.setTemplate(templateConverter.map(entity.getTemplate()));
            dto.setSessionStatusId(entity.getStatus().getSessionStatusId());

            if (nonNull(entity.getReviewer())) {
                dto.setReviewerId(entity.getReviewer().getId());
            }
        }

        dto.setAnswers(entity.getAnswers().stream().map(answerConverter::map).collect(toList()));

        return dto;
    }
}

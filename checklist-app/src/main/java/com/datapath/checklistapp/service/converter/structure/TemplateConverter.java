package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.GroupQuestionsDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class TemplateConverter {

    private final QuestionConverter questionConverter;

    public TemplateDTO map(TemplateConfigEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigTypeId(entity.getType().getTypeId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = entity.getQuestionExecutions().stream()
                .filter(qe -> OBJECT_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found required object question"));

        dto.setObjectQuestion(questionConverter.map(objectQuestion));

        dto.setObjectFeatureQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> FEATURE_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> ACTIVITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()) ||
                                SESSION_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setAuthorityQuestions(
                entity.getQuestionExecutions().stream()
                        .filter(qe -> AUTHORITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        return dto;
    }

    public TemplateDTO map(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigId(entity.getConfig().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = entity.getConfig()
                .getQuestionExecutions()
                .stream()
                .filter(qe -> OBJECT_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Not found required object question"));

        dto.setObjectQuestion(questionConverter.map(objectQuestion));

        dto.setObjectFeatureQuestions(
                entity.getConfig().getQuestionExecutions().stream()
                        .filter(qe -> FEATURE_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getConfig().getQuestionExecutions().stream()
                        .filter(qe -> ACTIVITY_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()) ||
                                SESSION_QUESTION_TYPE.equals(qe.getQuestion().getType().getTypeId()))
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        Optional<QuestionGroupEntity> ungrouped = entity.getGroups().stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .findFirst();

        ungrouped.ifPresent(questionGroupEntity -> dto.setUngroupedQuestions(
                questionGroupEntity.getQuestions().stream()
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        ));

        dto.setQuestions(
                entity.getGroups().stream()
                        .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                        .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                        .map(g -> {
                                    GroupQuestionsDTO groupQuestionsDTO = new GroupQuestionsDTO();
                                    groupQuestionsDTO.setGroupName(g.getName());
                                    groupQuestionsDTO.setQuestions(
                                            g.getQuestions().stream()
                                                    .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                                                    .map(questionConverter::map)
                                                    .collect(toList())
                                    );
                                    return groupQuestionsDTO;
                                }
                        ).collect(toList())
        );

        return dto;
    }

    public List<TemplateFolderTreeDTO> joinFolderWithTemplates(Map<Long, List<TemplateDTO>> folderTemplatesMap,
                                                                Map<Long, FolderDTO> folders) {
        List<TemplateFolderTreeDTO> response = new ArrayList<>();

        folders.forEach((k, v) -> {
            List<TemplateDTO> folderTemplates = folderTemplatesMap.get(k);

            if (!isEmpty(folderTemplates)) {
                response.add(new TemplateFolderTreeDTO(v, folderTemplates));
            }
        });

        return response;
    }
}

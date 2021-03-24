package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.GroupQuestionsDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.datapath.checklistapp.util.Constants.UNGROUPED_NAME;
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

        dto.setObjectQuestion(questionConverter.map(entity.getObjectQuestionExecution()));

        dto.setFeatureQuestions(
                entity.getFutureQuestionExecutions().stream()
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getTypeQuestionExecutions().stream()
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setAuthorityQuestions(
                entity.getAuthorityQuestionExecutions().stream()
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

        dto.setObjectQuestion(questionConverter.map(entity.getConfig().getObjectQuestionExecution()));

        dto.setFeatureQuestions(
                entity.getConfig().getFutureQuestionExecutions().stream()
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(questionConverter::map)
                        .collect(toList())
        );

        dto.setTypeQuestions(
                entity.getConfig().getTypeQuestionExecutions().stream()
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

    public TemplateDTO shortMap(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateConfigId(entity.getConfig().getId());
        return dto;
    }

    public TemplateDTO shortMap(TemplateConfigEntity entity) {
        TemplateDTO dto = new TemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateConfigTypeId(entity.getType().getTypeId());
        return dto;
    }
}

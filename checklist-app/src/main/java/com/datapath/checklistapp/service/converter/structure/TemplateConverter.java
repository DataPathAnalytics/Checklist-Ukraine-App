package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.UNGROUPED_NAME;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;
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

        dto.setObjectQuestion(processQuestionWithChild(entity.getObjectQuestion(), entity.getObjectFutureQuestions()));
        dto.setObjectFeatureQuestions(processQuestions(entity.getObjectFutureQuestions()));

        dto.setTypeQuestions(processQuestions(entity.getTypeQuestions()));

        if (nonNull(entity.getAuthorityQuestion())) {
            dto.setAuthorityQuestion(processQuestionWithChild(entity.getAuthorityQuestion(), entity.getAuthorityFeatureQuestions()));
            dto.setAuthorityFeatureQuestions(processQuestions(entity.getAuthorityFeatureQuestions()));
        }

        return dto;
    }

    public TemplateDTO map(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigId(entity.getConfig().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        dto.setObjectQuestion(processQuestionWithChild(entity.getConfig().getObjectQuestion(), entity.getConfig().getObjectFutureQuestions()));
        dto.setObjectFeatureQuestions(processQuestions(entity.getConfig().getObjectFutureQuestions()));

        dto.setTypeQuestions(processQuestions(entity.getConfig().getTypeQuestions()));

        Optional<QuestionGroupEntity> ungrouped = entity.getGroups().stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .findFirst();

        ungrouped.ifPresent(questionGroupEntity -> dto.setUngroupedQuestions(
                processQuestions(questionGroupEntity.getQuestions())
        ));

        List<GroupQuestionsDTO> groups = new ArrayList<>();
        entity.getGroups().stream()
                .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                .forEach(qg -> {
                    GroupQuestionsDTO group = new GroupQuestionsDTO();
                    group.setGroupName(qg.getName());
                    group.setOrderNumber(qg.getOrderNumber());
                    group.setQuestions(processQuestions(qg.getQuestions()));
                    groups.add(group);
                });
        dto.setQuestionGroups(groups);

        return dto;
    }

    private QuestionExecutionDTO processQuestionWithChild(QuestionExecutionEntity questionEntity,
                                                          Set<QuestionExecutionEntity> subQuestions) {
        QuestionExecutionDTO question = questionConverter.map(questionEntity);
        question.addSubQuestions(
                subQuestions.stream()
                        .filter(QuestionExecutionEntity::isRoot)
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(QuestionExecutionEntity::getId)
                        .collect(toSet())
        );
        return question;
    }

    private List<QuestionExecutionDTO> processQuestions(Set<QuestionExecutionEntity> questions) {
        Map<Long, QuestionExecutionDTO> questionMap = questions
                .stream()
                .map(questionConverter::map)
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        questions.stream()
                .filter(q -> !q.isRoot())
                .forEach(q -> questionMap.get(q.getParentQuestionId())
                        .addSubQuestion(q.getId(), q.getConditionFieldName(), q.getConditionAnswerId()));

        return questionMap.values().stream()
                .sorted(Comparator.comparing(QuestionExecutionDTO::getOrderNumber))
                .collect(toList());
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

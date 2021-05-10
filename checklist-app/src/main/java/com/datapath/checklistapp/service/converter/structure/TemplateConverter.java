package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dto.*;
import com.datapath.checklistapp.util.database.TemplateRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.UNGROUPED_NAME;
import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class TemplateConverter {

    private final QuestionConverter questionConverter;

    public TemplateDTO map(TemplateConfigEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigTypeId(entity.getType().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = filterQuestionExecutionByRole(entity.getQuestions(), TemplateRole.OBJECT).get(0);
        List<QuestionExecutionEntity> objectFeatureQuestions = filterQuestionExecutionByRole(entity.getQuestions(), TemplateRole.OBJECT_FUTURE);
        List<QuestionExecutionEntity> typeQuestions = filterQuestionExecutionByRole(entity.getQuestions(), TemplateRole.TYPE);

        dto.setObjectQuestion(processQuestionWithChild(objectQuestion, objectFeatureQuestions, null));
        dto.setObjectFeatureQuestions(processQuestions(objectFeatureQuestions));
        dto.setTypeQuestions(processQuestions(typeQuestions));

        List<QuestionExecutionEntity> authorityQuestions = filterQuestionExecutionByRole(entity.getQuestions(), TemplateRole.AUTHORITY);

        if (!isEmpty(authorityQuestions)) {
            QuestionExecutionEntity authorityQuestion = authorityQuestions.get(0);
            List<QuestionExecutionEntity> authorityFeatureQuestions = filterQuestionExecutionByRole(entity.getQuestions(), TemplateRole.AUTHORITY_FEATURE);

            dto.setAuthorityQuestion(processQuestionWithChild(authorityQuestion, authorityFeatureQuestions, null));
            dto.setAuthorityFeatureQuestions(processQuestions(authorityFeatureQuestions));
        }

        return dto;
    }

    public TemplateDTO map(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigId(entity.getConfig().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        QuestionExecutionEntity objectQuestion = filterQuestionExecutionByRole(entity.getConfig().getQuestions(), TemplateRole.OBJECT).get(0);
        List<QuestionExecutionEntity> objectFeatureQuestions = filterQuestionExecutionByRole(entity.getConfig().getQuestions(), TemplateRole.OBJECT_FUTURE);
        List<QuestionExecutionEntity> typeQuestions = filterQuestionExecutionByRole(entity.getConfig().getQuestions(), TemplateRole.TYPE);


        dto.setObjectQuestion(processQuestionWithChild(objectQuestion, objectFeatureQuestions, entity.getGroups()));
        dto.setObjectFeatureQuestions(processQuestions(objectFeatureQuestions));
        dto.setTypeQuestions(processQuestions(typeQuestions));

        Optional<QuestionGroupEntity> ungrouped = entity.getGroups().stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .findFirst();

        ungrouped.ifPresent(questionGroupEntity -> dto.setUngroupedQuestions(
                processQuestions(new ArrayList<>(questionGroupEntity.getQuestions()))
        ));

        List<GroupQuestionsDTO> groups = new ArrayList<>();
        entity.getGroups().stream()
                .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                .forEach(qg -> {
                    GroupQuestionsDTO group = new GroupQuestionsDTO();
                    group.setGroupName(qg.getName());
                    group.setOrderNumber(qg.getOrderNumber());
                    group.setQuestions(processQuestions(new ArrayList<>(qg.getQuestions())));
                    groups.add(group);
                });
        dto.setQuestionGroups(groups);

        return dto;
    }

    private List<QuestionExecutionEntity> filterQuestionExecutionByRole(Set<QuestionExecutionEntity> questionExecutions, TemplateRole role) {
        return questionExecutions.stream()
                .filter(q -> q.getRole().equals(role))
                .collect(toList());
    }

    private QuestionExecutionDTO processQuestionWithChild(QuestionExecutionEntity questionEntity,
                                                          List<QuestionExecutionEntity> subQuestions, Set<QuestionGroupEntity> groups) {
        QuestionExecutionDTO question = questionConverter.map(questionEntity);
        question.addSubQuestions(
                subQuestions.stream()
                        .filter(QuestionExecutionEntity::isRoot)
                        .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                        .map(QuestionExecutionEntity::getId)
                        .collect(toSet())
        );

        if (!isEmpty(groups)) {
            groups.forEach(group -> question.addSubQuestions(group.getQuestions().stream()
                    .filter(QuestionExecutionEntity::isRoot)
                    .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                    .map(QuestionExecutionEntity::getId)
                    .collect(toSet())));
        }

        return question;
    }

    private List<QuestionExecutionDTO> processQuestions(List<QuestionExecutionEntity> questions) {
        Map<Integer, QuestionExecutionDTO> questionMap = questions
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

    public List<TemplateFolderTreeDTO> joinFolderWithTemplates(Map<Integer, List<TemplateDTO>> folderTemplatesMap,
                                                               Map<Integer, FolderDTO> folders) {
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
        dto.setTemplateConfigTypeId(entity.getType().getId());
        return dto;
    }
}

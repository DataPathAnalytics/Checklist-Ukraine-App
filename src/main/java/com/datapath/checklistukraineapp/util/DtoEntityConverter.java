package com.datapath.checklistukraineapp.util;

import com.datapath.checklistukraineapp.dao.entity.*;
import com.datapath.checklistukraineapp.dto.*;
import com.datapath.checklistukraineapp.exception.ValidationException;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;

public class DtoEntityConverter {

    public static ResponseSessionDTO map(ResponseSessionEntity entity) {
        ResponseSessionDTO dto = mapResponseSessionBaseInfo(entity);

        Map<Long, AnswerEntity> answerQuestionId = entity.getAnswers()
                .stream()
                .collect(toMap(a -> a.getQuestionExecution().getId(), Function.identity()));

        QuestionExecutionEntity objectQuestion = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> OBJECT_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Required object question not found. Response session id: " + entity.getId()));

        List<QuestionExecutionDTO> featureQuestions = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> OBJECT_FEATURE_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .map(q -> map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> typeQuestions = entity.getTemplate().getConfig().getQuestionExecutions()
                .stream()
                .filter(q -> SESSION_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .map(q -> map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> ungroupedQuestions = entity.getTemplate().getGroups()
                .stream()
                .filter(g -> UNGROUPED_NAME.equals(g.getName()))
                .flatMap(g -> g.getQuestions().stream())
                .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                .map(q -> map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<GroupQuestionsDTO> questions = entity.getTemplate().getGroups()
                .stream()
                .filter(g -> !UNGROUPED_NAME.equals(g.getName()))
                .sorted(Comparator.comparing(QuestionGroupEntity::getOrderNumber))
                .map(g -> {
                    GroupQuestionsDTO groupQuestionsDTO = new GroupQuestionsDTO();
                    groupQuestionsDTO.setGroupName(g.getName());
                    groupQuestionsDTO.setQuestions(
                            g.getQuestions().stream()
                                    .sorted(Comparator.comparing(QuestionExecutionEntity::getOrderNumber))
                                    .map(q -> map(q, answerQuestionId.get(q.getId())))
                                    .collect(toList())
                    );
                    return groupQuestionsDTO;
                })
                .collect(toList());

        dto.setObjectQuestion(map(objectQuestion, answerQuestionId.get(objectQuestion.getId())));
        dto.setObjectFeatureQuestions(featureQuestions);
        dto.setTypeQuestions(typeQuestions);
        dto.setQuestions(questions);
        dto.setUngroupedQuestions(ungroupedQuestions);

        return dto;
    }

    private static ResponseSessionDTO mapResponseSessionBaseInfo(ResponseSessionEntity entity) {
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

    public static ControlActivityDTO map(ControlActivityEntity activity) {
        ControlActivityDTO dto = new ControlActivityDTO();

        BeanUtils.copyProperties(activity, dto);
        dto.setMembers(
                activity.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
        dto.setAuthorId(activity.getAuthor().getId());
        dto.setStatusId(activity.getStatus().getActivityStatusId());
        dto.setAuthorityId(activity.getAuthority().getAuthorityId());
        dto.setTemplates(
                activity.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        Map<Long, AnswerEntity> answerQuestionId = activity.getActivityResponse().getAnswers()
                .stream()
                .collect(toMap(a -> a.getQuestionExecution().getId(), Function.identity()));

        List<QuestionExecutionDTO> featureQuestions = activity.getActivityResponse()
                .getTemplateConfig()
                .getQuestionExecutions()
                .stream()
                .filter(q -> OBJECT_FEATURE_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .map(q -> map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> typeQuestions = activity.getActivityResponse()
                .getTemplateConfig()
                .getQuestionExecutions()
                .stream()
                .filter(q -> ACTIVITY_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .map(q -> map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        QuestionExecutionEntity objectQuestion = activity.getActivityResponse()
                .getTemplateConfig()
                .getQuestionExecutions()
                .stream()
                .filter(q -> OBJECT_QUESTION_TYPE.equals(q.getQuestion().getType().getQuestionTypeId()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Required object question not found. Control activity id: " + activity.getId()));

        dto.setObjectQuestion(map(objectQuestion, answerQuestionId.get(objectQuestion.getId())));
        dto.setObjectFeatureQuestions(featureQuestions);
        dto.setTypeQuestions(typeQuestions);

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
                        .map(DtoEntityConverter::mapResponseSessionBaseInfo)
                        .collect(toList())
        );

        dto.setSessions(sessionPage);

        return dto;
    }

    public static QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeCategoryId(entity.getKnowledgeCategory().getKnowledgeCategoryId());
        dto.setKnowledgeCategoryValue(entity.getKnowledgeCategory().getValue());
        dto.setKnowledgeCategoryTranslate(entity.getKnowledgeCategory().getTranslate());

        dto.setQuestionTypeId(entity.getType().getQuestionTypeId());
        dto.setQuestionTypeValue(entity.getType().getValue());

        dto.setQuestionSourceId(entity.getSource().getSource().getId());
        dto.setQuestionSourceName(entity.getSource().getSource().getName());
        dto.setQuestionSourceLink(entity.getSource().getSource().getLink());
        dto.setDocumentParagraph(entity.getSource().getDocumentParagraph());

        if (nonNull(entity.getAnswerStructure())) {
            dto.setAnswerStructure(map(entity.getAnswerStructure()));
        }

        return dto;
    }

    public static QuestionExecutionDTO map(QuestionExecutionEntity entity) {
        QuestionExecutionDTO dto = new QuestionExecutionDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setQuestion(map(entity.getQuestion()));

        return dto;
    }

    public static AnswerStructureDTO map(AnswerStructureEntity entity) {
        AnswerStructureDTO dto = new AnswerStructureDTO();

        dto.setId(entity.getId());
        dto.setLinkType(entity.getLinkType());
        dto.setName(entity.getName());
        dto.setFieldDescriptions(
                entity.getFields().stream()
                        .map(f -> new AnswerStructureDTO.FieldDescriptionDTO(
                                f.getName(),
                                f.getDescription(),
                                f.getDataSource(),
                                f.getType())
                        ).collect(toList())
        );

        return dto;
    }

    public static TemplateDTO map(TemplateConfigEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateType(entity.getType().getTemplateTypeId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        return dto;
    }

    public static TemplateDTO map(TemplateEntity entity) {
        TemplateDTO dto = new TemplateDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setTemplateConfigId(entity.getConfig().getId());
        dto.setFolderId(entity.getFolder().getId());
        dto.setAuthorId(entity.getAuthor().getId());

        return dto;
    }

    public static AnswerDTO map(AnswerEntity entity) {
        if (isNull(entity)) return null;

        AnswerDTO dto = new AnswerDTO();

        if (nonNull(entity.getAnswerType())) {
            dto.setAnswerTypeId(entity.getAnswerType().getAnswerTypeId());
        }
        dto.setComment(entity.getComment());
        dto.setValues(entity.getValues());

        return dto;
    }

    public static QuestionExecutionDTO map(QuestionExecutionEntity entity, AnswerEntity answerEntity) {
        QuestionExecutionDTO executionDTO = new QuestionExecutionDTO();

        QuestionExecutionDTO objectQuestionDto = new QuestionExecutionDTO();
        objectQuestionDto.setQuestion(map(entity.getQuestion()));
        objectQuestionDto.setAnswer(map(answerEntity));
        objectQuestionDto.setConditionAnswerId(entity.getConditionAnswerId());
        objectQuestionDto.setId(entity.getId());
        objectQuestionDto.setParentFeatureId(entity.getParentFeatureId());
        objectQuestionDto.setParentQuestionId(entity.getParentQuestionId());
        return executionDTO;
    }
}

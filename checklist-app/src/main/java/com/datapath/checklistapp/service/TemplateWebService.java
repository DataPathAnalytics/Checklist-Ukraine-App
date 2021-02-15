package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.relatioship.InterpretationRelationship;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dao.service.classifier.TemplateTypeDaoService;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.converter.structure.TemplateConverter;
import com.datapath.checklistapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.*;
import static com.datapath.checklistapp.util.UserUtils.getCurrentUserId;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class TemplateWebService {

    private final TemplateDaoService templateService;
    private final TemplateConfigDaoService templateConfigService;
    private final UserDaoService userService;
    private final FolderDaoService folderService;
    private final QuestionDaoService questionService;
    private final TemplateTypeDaoService templateTypeService;
    private final TemplateConverter templateConverter;
    private final InterpretationDaoService interpretationService;

    @Transactional
    public void create(CreateTemplateRequest request) {
        TemplateEntity entity = new TemplateEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(getCurrentUserId()));
        entity.setFolder(folderService.findTemplateFolderById(request.getFolderId()));
        entity.setConfig(templateConfigService.findById(request.getTemplateConfigId()));

        if (!isEmpty(request.getUngroupedQuestions())) {
            QuestionGroupEntity ungroupedEntity = new QuestionGroupEntity();

            ungroupedEntity.setName(UNGROUPED_NAME);

            request.getUngroupedQuestions().forEach(q -> {
                QuestionExecutionEntity questionExecutionEntity = new QuestionExecutionEntity();
                questionExecutionEntity.setOrderNumber(q.getOrderNumber());
                questionExecutionEntity.setParentQuestionId(q.getParentQuestionId());
                questionExecutionEntity.setConditionAnswerId(q.getParentConditionAnswerId());
                questionExecutionEntity.setQuestion(questionService.findById(q.getQuestionId()));

                if (nonNull(q.getInterpretationId())) {
                    InterpretationRelationship interpretationRel = new InterpretationRelationship();
                    interpretationRel.setInterpretation(interpretationService.findById(q.getInterpretationId()));
                    interpretationRel.setConditionAnswer(q.getInterpretationConditionAnswerId());
                    questionExecutionEntity.setInterpretationRel(interpretationRel);
                }

                ungroupedEntity.getQuestions().add(questionExecutionEntity);
            });

            entity.getGroups().add(ungroupedEntity);
        }

        if (!isEmpty(request.getQuestionGroups())) {
            request.getQuestionGroups().forEach(group -> {
                if (!isEmpty(group.getQuestions())) {
                    QuestionGroupEntity groupedEntity = new QuestionGroupEntity();
                    groupedEntity.setName(group.getName());
                    groupedEntity.setOrderNumber(group.getOrderNumber());

                    group.getQuestions().forEach(q -> {
                        QuestionExecutionEntity questionExecutionEntity = new QuestionExecutionEntity();
                        questionExecutionEntity.setOrderNumber(q.getOrderNumber());
                        questionExecutionEntity.setParentQuestionId(q.getParentQuestionId());
                        questionExecutionEntity.setConditionAnswerId(q.getParentConditionAnswerId());
                        questionExecutionEntity.setQuestion(questionService.findById(q.getQuestionId()));

                        if (nonNull(q.getInterpretationId())) {
                            InterpretationRelationship interpretationRel = new InterpretationRelationship();
                            interpretationRel.setInterpretation(interpretationService.findById(q.getInterpretationId()));
                            interpretationRel.setConditionAnswer(q.getInterpretationConditionAnswerId());
                            questionExecutionEntity.setInterpretationRel(interpretationRel);
                        }

                        groupedEntity.getQuestions().add(questionExecutionEntity);
                    });
                }
            });
        }
        templateService.save(entity);
    }

    @Transactional
    public void create(CreateTemplateConfigRequest request) {
        Set<Long> questionIds = new HashSet<>();
        questionIds.add(request.getObjectQuestionId());
        questionIds.addAll(request.getFeatureQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .collect(toList()));
        questionIds.addAll(request.getTypeQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .collect(toList()));
        questionIds.addAll(request.getAuthorityQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .collect(toList()));

        Map<Long, QuestionEntity> questionIdMap = questionService.findById(new ArrayList<>(questionIds))
                .stream()
                .collect(toMap(QuestionEntity::getId, Function.identity()));

        checkQuestionTypes(request, questionIdMap);

        TemplateConfigEntity entity = new TemplateConfigEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        entity.setType(templateTypeService.findById(request.getTemplateTypeId()));
        entity.setFolder(folderService.findTemplateConfigFolderById(request.getFolderId()));

        QuestionExecutionEntity objectExecution = new QuestionExecutionEntity();
        objectExecution.setQuestion(questionIdMap.get(request.getObjectQuestionId()));
        entity.getQuestionExecutions().add(objectExecution);

        request.getFeatureQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setParentQuestionId(q.getParentQuestionId());
                execution.setOrderNumber(q.getOrderNumber());
                entity.getQuestionExecutions().add(execution);
            }
        });

        request.getTypeQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                entity.getQuestionExecutions().add(execution);
            }
        });

        request.getAuthorityQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                entity.getQuestionExecutions().add(execution);
            }
        });

        templateConfigService.save(entity);
    }

    public List<TemplateFolderTreeDTO> configList(Integer templateType) {
        List<TemplateConfigEntity> entities;

        if (nonNull(templateType)) {
            entities = templateConfigService.findAllByTemplateType(templateTypeService.findById(templateType));
        } else {
            entities = templateConfigService.findAll();
        }

        Map<Long, List<TemplateDTO>> folderTemplatesMap = entities.stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    dto.setFolderId(t.getFolder().getId());
                    dto.setAuthorId(t.getAuthor().getId());
                    dto.setTemplateType(t.getType().getTemplateTypeId());
                    return dto;
                })
                .collect(groupingBy(TemplateDTO::getFolderId));

        Map<Long, FolderDTO> folders = folderService.findAllTemplateConfigFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

        return joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public List<TemplateFolderTreeDTO> list() {
        Map<Long, List<TemplateDTO>> folderTemplatesMap = templateService.findAll()
                .stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    dto.setFolderId(t.getFolder().getId());
                    dto.setAuthorId(t.getAuthor().getId());
                    dto.setTemplateConfigId(t.getConfig().getId());
                    return dto;
                })
                .collect(groupingBy(TemplateDTO::getFolderId));

        Map<Long, FolderDTO> folders = folderService.findAllTemplateFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

        return joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public TemplateDTO get(Long id) {
        return templateConverter.map(templateService.findById(id));
    }

    public TemplateDTO getConfig(Long id) {
        return templateConverter.map(templateConfigService.findById(id));
    }

    private List<TemplateFolderTreeDTO> joinFolderWithTemplates(Map<Long, List<TemplateDTO>> folderTemplatesMap,
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

    private void checkQuestionTypes(CreateTemplateConfigRequest request, Map<Long, QuestionEntity> questionIdMap) {
        QuestionEntity objectQuestion = questionIdMap.get(request.getObjectQuestionId());
        if (isNull(objectQuestion))
            throw new EntityNotFoundException("question", request.getObjectQuestionId());
        if (!OBJECT_QUESTION_TYPE.equals(objectQuestion.getType().getQuestionTypeId()))
            throw new ValidationException("Invalid type of base question. Should be questionTypeId " + OBJECT_QUESTION_TYPE);

        checkQuestionTypes(request.getFeatureQuestions(), questionIdMap, FEATURE_QUESTION_TYPE);
        checkQuestionTypes(request.getAuthorityQuestions(), questionIdMap, AUTHORITY_QUESTION_TYPE);

        if (ACTIVITY_TEMPLATE_TYPE.equals(request.getTemplateTypeId())) {
            checkQuestionTypes(request.getTypeQuestions(), questionIdMap, ACTIVITY_QUESTION_TYPE);
        } else if (SESSION_TEMPLATE_TYPE.equals(request.getTemplateTypeId())) {
            checkQuestionTypes(request.getTypeQuestions(), questionIdMap, SESSION_QUESTION_TYPE);
        } else throw new ValidationException("Invalid template type: " + request.getTemplateTypeId());
    }

    private void checkQuestionTypes(List<CreateTemplateConfigRequest.TemplateQuestion> questions,
                                    Map<Long, QuestionEntity> questionIdMap,
                                    Integer checkedType) {
        boolean existsIncorrect = questions.stream()
                .map(questionIdMap::get)
                .filter(Objects::nonNull)
                .anyMatch(q -> !checkedType.equals(q.getType().getQuestionTypeId()));
        if (existsIncorrect)
            throw new ValidationException("Invalid question type. Should be questionTypeId " + checkedType);
    }
}

package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dao.service.classifier.TemplateTypeDaoService;
import com.datapath.checklistukraineapp.dto.*;
import com.datapath.checklistukraineapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistukraineapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistukraineapp.dto.response.TemplateResponse;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.exception.ValidationException;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistukraineapp.util.Constants.*;
import static com.datapath.checklistukraineapp.util.UserUtils.getCurrentUserId;
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

    @Transactional
    public void create(CreateTemplateRequest request) {
        TemplateEntity entity = new TemplateEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(getCurrentUserId()));
//        entity.setFolder(templateFolderService.findById(request.getFolderId()));
//        entity.setOwnerType(ownerTypeService.findById(request.getOwnerTypeId()));
//        entity.setSubjectType(subjectTypeService.findById(request.getSubjectTypeId()));

        request.getFactQuestions().forEach(q -> {
            QuestionEntity question = questionService.findById(q.getQuestionId());

            TemplateQuestionRelationship relationship = new TemplateQuestionRelationship();
            relationship.setGroupName(q.getGroupName());
            relationship.setQuestion(question);

            if (nonNull(q.getParentQuestionId())) {
                QuestionEntity parentQuestion = questionService.findById(q.getParentQuestionId());
                relationship.setParentQuestionId(parentQuestion.getId());
                relationship.setAnswerId(q.getParentAnswerId());
            }

            entity.getFactQuestions().add(relationship);
        });

        entity.getOntologyQuestions().addAll(
                questionService.findByIds(request.getOntologyQuestions())
                        .stream()
//                        .filter(q -> request.getSubjectTypeId().equals(q.getSubjectType().getId()))
                        .collect(toList())
        );

        templateService.save(entity);
    }

    public List<TemplateFolderTreeDTO> list() {
        Map<Long, List<TemplateDTO>> folderTemplatesMap = templateService.findAll()
                .stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    dto.setFolderId(t.getFolder().getId());
                    dto.setAuthorId(t.getAuthor().getId());
                    return dto;
                })
                .collect(groupingBy(TemplateDTO::getFolderId));

        return joinTemplateWithFolder(folderTemplatesMap);
    }

    public List<TemplateFolderTreeDTO> list(Long ownerTypeId) {
//        Map<Long, List<TemplateDTO>> folderTemplatesMap = templateService.findByOwnerType(ownerTypeService.findById(ownerTypeId))
//                .stream()
//                .map(t -> {
//                    TemplateDTO dto = new TemplateDTO();
//                    BeanUtils.copyProperties(t, dto);
//                    dto.setFolderId(t.getFolder().getId());
//                    dto.setAuthorId(t.getAuthor().getId());
//                    return dto;
//                })
//                .collect(groupingBy(TemplateDTO::getFolderId));
//
//        return joinTemplateWithFolder(folderTemplatesMap);
        return null;
    }

    @Transactional
    public TemplateResponse get(Long id) {
        TemplateResponse response = new TemplateResponse();

        TemplateEntity entity = templateService.findById(id);

        TemplateDTO template = new TemplateDTO();
        BeanUtils.copyProperties(entity, template);

        response.setTemplate(template);

        response.setGroupQuestions(
                entity.getFactQuestions().stream()
                        .map(q -> {
                            TemplateQuestionDTO dto = new TemplateQuestionDTO();
                            dto.setQuestionId(q.getId());
                            dto.setGroupName(q.getGroupName());
                            dto.setName(q.getQuestion().getName());
                            dto.setParentAnswerId(q.getAnswerId());
                            dto.setParentQuestionId(q.getParentQuestionId());
                            dto.setQuestionSourceId(q.getQuestion().getSource().getSource().getId());
                            dto.setDocumentParagraph(q.getQuestion().getSource().getDocumentParagraph());
                            dto.setQuestionSourceLink(q.getQuestion().getSource().getSource().getLink());
                            dto.setQuestionSourceName(q.getQuestion().getSource().getSource().getName());
                            return dto;
                        }).collect(groupingBy(TemplateQuestionDTO::getGroupName))
                        .entrySet()
                        .stream()
                        .map(gq -> new GroupQuestionsDTO(gq.getKey(), gq.getValue()))
                        .collect(toList())
        );

        return response;
    }

    private List<TemplateFolderTreeDTO> joinTemplateWithFolder(Map<Long, List<TemplateDTO>> folderTemplatesMap) {
        Map<Long, FolderDTO> folders = folderService.findAllTemplateFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

        List<TemplateFolderTreeDTO> response = new ArrayList<>();

        folders.forEach((k, v) -> {
            List<TemplateDTO> folderTemplates = folderTemplatesMap.get(k);

            if (!isEmpty(folderTemplates)) {
                response.add(new TemplateFolderTreeDTO(v, folderTemplates));
            }
        });

        return response;
    }

    @Transactional
    public void createConfig(CreateTemplateConfigRequest request) {
        Set<Long> questionIds = new HashSet<>();
        questionIds.add(request.getObjectQuestionId());
        questionIds.addAll(request.getFeatureQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .collect(toList()));
        questionIds.addAll(request.getQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .collect(toList()));

        Map<Long, QuestionEntity> questionIdMap = questionService.findByIds(new ArrayList<>(questionIds))
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
                execution.setParentFeatureId(q.getParentFeatureId());
                execution.setOrderNumber(q.getOrderNumber());
                entity.getQuestionExecutions().add(execution);
            }
        });

        templateConfigService.save(entity);
    }

    private void checkQuestionTypes(CreateTemplateConfigRequest request, Map<Long, QuestionEntity> questionIdMap) {
        QuestionEntity objectQuestion = questionIdMap.get(request.getObjectQuestionId());
        if (isNull(objectQuestion))
            throw new EntityNotFoundException("question", request.getObjectQuestionId());
        if (!OBJECT_QUESTION_TYPE.equals(objectQuestion.getType().getQuestionTypeId()))
            throw new ValidationException("Invalid type of base question. Should be id " + OBJECT_QUESTION_TYPE);

        checkQuestionTypes(request.getFeatureQuestions(), questionIdMap, OBJECT_FEATURE_QUESTION_TYPE);

        if (ACTIVITY_TEMPLATE_TYPE.equals(request.getTemplateTypeId())) {
            checkQuestionTypes(request.getQuestions(), questionIdMap, ACTIVITY_QUESTION_TYPE);
        } else if (SESSION_TEMPLATE_TYPE.equals(request.getTemplateTypeId())) {
            checkQuestionTypes(request.getQuestions(), questionIdMap, SESSION_QUESTION_TYPE);
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
            throw new ValidationException("Invalid question type. Should be id" + checkedType);
    }
}

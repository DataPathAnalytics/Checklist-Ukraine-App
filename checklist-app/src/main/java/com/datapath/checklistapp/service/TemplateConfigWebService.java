package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dao.service.classifier.TemplateConfigTypeDaoService;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.exception.UnmodifiedException;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.converter.structure.TemplateConverter;
import com.datapath.checklistapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class TemplateConfigWebService {

    private final TemplateConfigDaoService templateConfigService;
    private final UserDaoService userService;
    private final FolderDaoService folderService;
    private final QuestionDaoService questionService;
    private final TemplateConfigTypeDaoService templateTypeService;
    private final TemplateConverter templateConverter;
    private final QuestionExecutionDaoService questionExecutionService;

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
        entity.setType(templateTypeService.findById(request.getTemplateConfigTypeId()));
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
                execution.setRequired(q.isRequired());
                entity.getQuestionExecutions().add(execution);
            }
        });

        request.getTypeQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                execution.setRequired(q.isRequired());
                entity.getQuestionExecutions().add(execution);
            }
        });

        request.getAuthorityQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                execution.setRequired(q.isRequired());
                entity.getQuestionExecutions().add(execution);
            }
        });

        templateConfigService.save(entity);
    }

    public List<TemplateFolderTreeDTO> list(Integer templateType) {
        List<TemplateConfigEntity> entities;

        if (nonNull(templateType)) {
            entities = templateConfigService.findAllByType(templateTypeService.findById(templateType));
        } else {
            entities = templateConfigService.findAll();
        }

        Map<Long, List<TemplateDTO>> folderTemplatesMap = entities.stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    dto.setFolderId(t.getFolder().getId());
                    dto.setAuthorId(t.getAuthor().getId());
                    dto.setTemplateConfigTypeId(t.getType().getTypeId());
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

        return templateConverter.joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public TemplateDTO get(Long id) {
        return templateConverter.map(templateConfigService.findById(id));
    }

    private void checkQuestionTypes(CreateTemplateConfigRequest request, Map<Long, QuestionEntity> questionIdMap) {
        QuestionEntity objectQuestion = questionIdMap.get(request.getObjectQuestionId());
        if (isNull(objectQuestion))
            throw new EntityNotFoundException("question", request.getObjectQuestionId());
        if (!OBJECT_QUESTION_TYPE.equals(objectQuestion.getType().getTypeId()))
            throw new ValidationException("Invalid type of base question. Should be " + OBJECT_QUESTION_TYPE);

        checkQuestionTypes(request.getFeatureQuestions(), questionIdMap, FEATURE_QUESTION_TYPE);
        checkQuestionTypes(request.getAuthorityQuestions(), questionIdMap, AUTHORITY_QUESTION_TYPE);

        if (ACTIVITY_TEMPLATE_TYPE.equals(request.getTemplateConfigTypeId())) {
            checkQuestionTypes(request.getTypeQuestions(), questionIdMap, ACTIVITY_QUESTION_TYPE);
        } else if (SESSION_TEMPLATE_TYPE.equals(request.getTemplateConfigTypeId())) {
            checkQuestionTypes(request.getTypeQuestions(), questionIdMap, SESSION_QUESTION_TYPE);
        } else throw new ValidationException("Invalid template type: " + request.getTemplateConfigTypeId());
    }

    private void checkQuestionTypes(List<CreateTemplateConfigRequest.TemplateQuestion> questions,
                                    Map<Long, QuestionEntity> questionIdMap,
                                    Integer checkedType) {
        boolean existsIncorrect = questions.stream()
                .map(questionIdMap::get)
                .filter(Objects::nonNull)
                .anyMatch(q -> !checkedType.equals(q.getType().getTypeId()));
        if (existsIncorrect)
            throw new ValidationException("Invalid question type. Should be " + checkedType);
    }

    public PageableResponse<TemplateDTO> search(SearchRequest request) {
        Page<TemplateConfigEntity> page = templateConfigService.searchByName(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(t -> {
                            TemplateDTO dto = new TemplateDTO();
                            BeanUtils.copyProperties(t, dto);
                            dto.setFolderId(t.getFolder().getId());
                            dto.setAuthorId(t.getAuthor().getId());
                            dto.setTemplateConfigTypeId(t.getType().getTypeId());
                            return dto;
                        }).collect(toList())
        );
    }

    @Transactional
    public void delete(Long id) {
        if (templateConfigService.isUsed(id)) throw new UnmodifiedException("Template config already is used");

        TemplateConfigEntity entity = templateConfigService.findById(id);
        entity.getQuestionExecutions().forEach(questionExecutionService::delete);
        templateConfigService.delete(entity);
    }
}

package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.FieldDescriptionEntity;
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
        entity.setObjectQuestionExecution(objectExecution);

        request.getFeatureQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setParentQuestionId(q.getParentQuestionId());
                execution.setOrderNumber(q.getOrderNumber());
                execution.setRequired(q.isRequired());
                execution.setLinkType(q.getLinkType());
                entity.getFutureQuestionExecutions().add(execution);
            }
        });

        request.getTypeQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                execution.setRequired(q.isRequired());
                execution.setLinkType(q.getLinkType());
                entity.getTypeQuestionExecutions().add(execution);
            }
        });

        request.getAuthorityQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                QuestionExecutionEntity execution = new QuestionExecutionEntity();
                execution.setQuestion(question);
                execution.setOrderNumber(q.getOrderNumber());
                execution.setRequired(q.isRequired());
                execution.setLinkType(q.getLinkType());
                entity.getAuthorityQuestionExecutions().add(execution);
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

        if (!hasIdentifier(objectQuestion))
            throw new ValidationException("Invalid base question. Answer structure must has identifier fields");

        boolean validAuthorityQuestion = request.getAuthorityQuestions()
                .stream()
                .map(q -> questionIdMap.get(q.getQuestionId()))
                .anyMatch(this::hasIdentifier);

        if (!validAuthorityQuestion) {
            throw new ValidationException("Invalid authority question. Answer structure must has identifier fields");
        }
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

    private boolean hasIdentifier(QuestionEntity question) {
        return question.getAnswerStructure().getFields().stream()
                .anyMatch(FieldDescriptionEntity::isIdentifier);
    }

    @Transactional
    public void delete(Long id) {
        if (templateConfigService.isUsed(id)) throw new UnmodifiedException("Template config already is used");

        TemplateConfigEntity entity = templateConfigService.findById(id);
        entity.getTypeQuestionExecutions().forEach(questionExecutionService::delete);
        entity.getAuthorityQuestionExecutions().forEach(questionExecutionService::delete);
        entity.getFutureQuestionExecutions().forEach(questionExecutionService::delete);
        questionExecutionService.delete(entity.getObjectQuestionExecution());
        templateConfigService.delete(entity);
    }
}

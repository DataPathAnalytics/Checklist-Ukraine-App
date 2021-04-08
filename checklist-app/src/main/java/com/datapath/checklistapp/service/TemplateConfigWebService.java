package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.FieldDescriptionEntity;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
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
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
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
    private final QuestionConverter questionConverter;
    private final QuestionExecutionDaoService questionExecutionService;

    @Transactional
    public void create(CreateTemplateConfigRequest request) {
        Set<Long> questionIds = new HashSet<>();
        questionIds.add(request.getObjectQuestion().getQuestionId());
        questionIds.add(request.getAuthorityQuestion().getQuestionId());
        request.getObjectFeatureQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .forEach(questionIds::add);
        request.getTypeQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .forEach(questionIds::add);
        request.getAuthorityFeatureQuestions()
                .stream()
                .map(CreateTemplateConfigRequest.TemplateQuestion::getQuestionId)
                .forEach(questionIds::add);

        Map<Long, QuestionEntity> questionIdMap = questionService.findById(new ArrayList<>(questionIds))
                .stream()
                .collect(toMap(QuestionEntity::getId, Function.identity()));

        validateQuestions(request, questionIdMap);

        TemplateConfigEntity entity = new TemplateConfigEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        entity.setType(templateTypeService.findById(request.getTemplateConfigTypeId()));
        entity.setFolder(folderService.findTemplateConfigFolderById(request.getFolderId()));

        entity.setObjectQuestionExecution(
                questionConverter.map(
                        request.getObjectQuestion().asTemplateQuestion(),
                        questionIdMap.get(request.getObjectQuestion().getQuestionId())
                )
        );
        entity.setAuthorityQuestionExecution(
                questionConverter.map(
                        request.getAuthorityQuestion().asTemplateQuestion(),
                        questionIdMap.get(request.getAuthorityQuestion().getQuestionId())
                )
        );

        request.getObjectFeatureQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                entity.getObjectFutureQuestionExecutions().add(questionConverter.map(q, question));
            }
        });

        request.getTypeQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                entity.getTypeQuestionExecutions().add(questionConverter.map(q, question));
            }
        });

        request.getAuthorityFeatureQuestions().forEach(q -> {
            QuestionEntity question = questionIdMap.get(q.getQuestionId());
            if (nonNull(question)) {
                entity.getAuthorityFeatureQuestionExecutions().add(questionConverter.map(q, question));
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
                .map(templateConverter::shortMap)
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

    private void validateQuestions(CreateTemplateConfigRequest request, Map<Long, QuestionEntity> questionIdMap) {
        QuestionEntity objectQuestion = questionIdMap.get(request.getObjectQuestion().getQuestionId());

        if (!hasIdentifier(objectQuestion))
            throw new ValidationException("Invalid base question. Answer structure must has identifier fields");

        QuestionEntity authorityQuestion = questionIdMap.get(request.getAuthorityQuestion().getQuestionId());

        if (!hasIdentifier(authorityQuestion))
            throw new ValidationException("Invalid authority representative question. Answer structure must has identifier fields");
    }

    public PageableResponse<TemplateDTO> search(SearchRequest request) {
        Page<TemplateConfigEntity> page = templateConfigService.searchByName(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(templateConverter::shortMap)
                        .collect(toList())
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
        entity.getAuthorityFeatureQuestionExecutions().forEach(questionExecutionService::delete);
        entity.getObjectFutureQuestionExecutions().forEach(questionExecutionService::delete);
        questionExecutionService.delete(entity.getObjectQuestionExecution());
        questionExecutionService.delete(entity.getAuthorityQuestionExecution());
        templateConfigService.delete(entity);
    }
}

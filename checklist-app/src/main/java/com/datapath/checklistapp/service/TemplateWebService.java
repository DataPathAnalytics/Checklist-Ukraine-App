package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.UnmodifiedException;
import com.datapath.checklistapp.exception.ValidationException;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import com.datapath.checklistapp.service.converter.structure.TemplateConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.SESSION_TEMPLATE_TYPE;
import static com.datapath.checklistapp.util.Constants.UNGROUPED_NAME;
import static com.datapath.checklistapp.util.UserUtils.getCurrentUserId;
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
    private final TemplateConverter templateConverter;
    private final QuestionConverter questionConverter;
    private final QuestionExecutionDaoService questionExecutionService;
    private final QuestionGroupDaoService questionGroupService;

    @Transactional
    public void create(CreateTemplateRequest request) {
        TemplateEntity entity = new TemplateEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(getCurrentUserId()));
        entity.setFolder(folderService.findTemplateFolderById(request.getFolderId()));

        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());
        if (!SESSION_TEMPLATE_TYPE.equals(config.getType().getTypeId()))
            throw new ValidationException("Invalid template config type. Should by response session template config type.");
        entity.setConfig(config);

        if (!isEmpty(request.getUngroupedQuestions())) {
            QuestionGroupEntity group = new QuestionGroupEntity();
            group.setName(UNGROUPED_NAME);

            Set<QuestionExecutionEntity> question = new HashSet<>();

            request.getUngroupedQuestions().forEach(q -> processQuestion(
                    q,
                    questionService.findById(q.getQuestionId()),
                    question,
                    null,
                    null)
            );
            group.setQuestions(question);
            entity.getGroups().add(group);
        }

        if (!isEmpty(request.getQuestionGroups())) {
            request.getQuestionGroups().forEach(groupDTO -> {
                if (!isEmpty(groupDTO.getQuestions())) {
                    QuestionGroupEntity group = new QuestionGroupEntity();
                    group.setName(group.getName());
                    group.setOrderNumber(group.getOrderNumber());

                    Set<QuestionExecutionEntity> question = new HashSet<>();

                    groupDTO.getQuestions().forEach(q -> processQuestion(
                            q,
                            questionService.findById(q.getQuestionId()),
                            question,
                            null,
                            null)
                    );
                    group.setQuestions(question);
                    entity.getGroups().add(group);
                }
            });
        }
        templateService.save(entity);
    }

    public List<TemplateFolderTreeDTO> list() {
        Map<Long, List<TemplateDTO>> folderTemplatesMap = templateService.findAll()
                .stream()
                .map(templateConverter::shortMap)
                .collect(groupingBy(TemplateDTO::getFolderId));

        Map<Long, FolderDTO> folders = folderService.findAllTemplateFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

        return templateConverter.joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public TemplateDTO get(Long id) {
        return templateConverter.map(templateService.findById(id));
    }


    public PageableResponse<TemplateDTO> search(SearchRequest request) {
        Page<TemplateEntity> page = templateService.searchByName(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(templateConverter::shortMap)
                        .collect(toList())
        );
    }

    @Transactional
    public void delete(Long id) {
        if (templateService.isUsed(id)) throw new UnmodifiedException("Template already is used");

        TemplateEntity entity = templateService.findById(id);
        entity.getGroups().forEach(g -> {
            g.getQuestions().forEach(questionExecutionService::delete);
            questionGroupService.delete(g);
        });
        templateService.delete(entity);
    }

    private void processQuestion(CreateTemplateRequest.TemplateQuestion dtoQuestion,
                                 QuestionEntity daoQuestion,
                                 Set<QuestionExecutionEntity> questionExecutions,
                                 Long parentQuestionId,
                                 Long conditionAnswerId) {
        QuestionExecutionEntity question = questionConverter.map(dtoQuestion, daoQuestion);

        if (nonNull(parentQuestionId)) {
            question.setParentQuestionId(parentQuestionId);
            question.setConditionAnswerId(conditionAnswerId);
        } else {
            question.setRoot(true);
        }

        QuestionExecutionEntity saved = questionExecutionService.save(question);
        questionExecutions.add(saved);

        if (!isEmpty(dtoQuestion.getSubQuestions())) {
            dtoQuestion.getSubQuestions().forEach(s -> processQuestion(
                    s.getQuestion(),
                    questionService.findById(s.getQuestion().getQuestionId()),
                    questionExecutions,
                    saved.getId(),
                    s.getConditionAnswerId())
            );
        }
    }
}

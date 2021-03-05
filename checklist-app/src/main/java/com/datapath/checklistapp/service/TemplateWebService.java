package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.InterpretationEntity;
import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.entity.TemplateEntity;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.UnmodifiedException;
import com.datapath.checklistapp.service.converter.structure.TemplateConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
    private final QuestionExecutionDaoService questionExecutionService;
    private final QuestionGroupDaoService questionGroupService;

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
                questionExecutionEntity.setRequired(q.isRequired());
                questionExecutionEntity.setQuestion(questionService.findById(q.getQuestionId()));

                if (nonNull(q.getInterpretationId())) {
                    InterpretationEntity interpretation = new InterpretationEntity();
                    interpretation.setOuterId(q.getInterpretationId());
                    interpretation.setConditionAnswer(q.getInterpretationConditionAnswerId());
                    questionExecutionEntity.setInterpretation(interpretation);
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
                        questionExecutionEntity.setRequired(q.isRequired());
                        questionExecutionEntity.setQuestion(questionService.findById(q.getQuestionId()));

                        if (nonNull(q.getInterpretationId())) {
                            InterpretationEntity interpretation = new InterpretationEntity();
                            interpretation.setOuterId(q.getInterpretationId());
                            interpretation.setConditionAnswer(q.getInterpretationConditionAnswerId());
                            questionExecutionEntity.setInterpretation(interpretation);
                        }

                        groupedEntity.getQuestions().add(questionExecutionEntity);
                    });
                }
            });
        }
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
                        .map(t -> {
                            TemplateDTO dto = new TemplateDTO();
                            BeanUtils.copyProperties(t, dto);
                            dto.setFolderId(t.getFolder().getId());
                            dto.setAuthorId(t.getAuthor().getId());
                            dto.setTemplateConfigId(t.getConfig().getId());
                            return dto;
                        }).collect(toList())
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
}

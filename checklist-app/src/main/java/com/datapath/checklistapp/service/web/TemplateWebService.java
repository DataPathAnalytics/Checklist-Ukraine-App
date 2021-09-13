package com.datapath.checklistapp.service.web;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.SaveTemplateRequest;
import com.datapath.checklistapp.dto.request.template.SaveTemplateRequest.QuestionGroup;
import com.datapath.checklistapp.dto.request.template.SaveTemplateRequest.TemplateQuestion;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.UnmodifiedException;
import com.datapath.checklistapp.service.mapper.QuestionMapper;
import com.datapath.checklistapp.service.mapper.TemplateMapper;
import com.datapath.checklistapp.service.validation.ValidateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.checklistapp.dto.request.template.SaveTemplateRequest.toGroup;
import static com.datapath.checklistapp.util.Constants.UNGROUPED_NAME;
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
    private final TemplateMapper templateMapper;
    private final QuestionMapper questionMapper;
    private final QuestionExecutionDaoService questionExecutionService;
    private final ValidateService validateService;

    @Transactional
    public void create(SaveTemplateRequest request) {
        TemplateConfigEntity config = templateConfigService.findById(request.getTemplateConfigId());

        validateService.validate(config);

        TemplateEntity entity = new TemplateEntity();

        if (nonNull(request.getId())) {
            checkUsability(request.getId());
            entity.setId(request.getId());
        }

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(getCurrentUserId()));
        entity.setFolder(folderService.findTemplateFolderById(request.getFolderId()));
        entity.setConfig(config);

        if (!isEmpty(request.getQuestions()))
            entity.getGroups().add(handle(toGroup(UNGROUPED_NAME, request.getQuestions())));

        if (!isEmpty(request.getGroups()))
            entity.getGroups().addAll(handle(request.getGroups()));

        templateService.save(entity);
    }

    private List<QuestionGroupEntity> handle(List<QuestionGroup> groups) {
        return groups.stream()
                .filter(g -> !isEmpty(g.getQuestions()))
                .map(this::handle)
                .collect(toList());
    }

    private QuestionGroupEntity handle(QuestionGroup dto) {
        QuestionGroupEntity group = new QuestionGroupEntity();
        group.setName(dto.getName());
        group.setOrderNumber(dto.getOrderNumber());

        List<QuestionExecutionEntity> entities = new ArrayList<>();

        List<TemplateQuestion> roots = dto.getQuestions().stream()
                .filter(q -> isNull(q.getParentHash()))
                .collect(toList());

        processQuestions(roots, dto.getQuestions(), entities, null);

        group.getQuestions().addAll(entities);
        return group;
    }

    private void processQuestions(List<TemplateQuestion> dtos,
                                  List<TemplateQuestion> allDtos,
                                  List<QuestionExecutionEntity> entities,
                                  Integer parentQuestionId) {
        dtos.forEach(q -> {
            QuestionEntity qEntity = questionService.findById(q.getQuestionId());
            QuestionExecutionEntity qexEntity = questionMapper.map(q, qEntity);

            if (nonNull(parentQuestionId)) {
                qexEntity.setParentQuestionId(parentQuestionId);
                qexEntity.setConditionAnswerId(q.getConditionAnswerId());
                qexEntity.setConditionFieldName(q.getConditionFieldName());
            } else {
                qexEntity.setRoot(true);
            }

            QuestionExecutionEntity saved = questionExecutionService.save(qexEntity);
            entities.add(saved);

            List<TemplateQuestion> children = getByParentHash(allDtos, q.getHash());
            if (!isEmpty(children)) processQuestions(children, allDtos, entities, saved.getId());
        });
    }

    private List<TemplateQuestion> getByParentHash(List<TemplateQuestion> questions,
                                                   String parentHash) {
        return questions.stream()
                .filter(q -> parentHash.equals(q.getParentHash()))
                .collect(toList());
    }

    public List<TemplateFolderTreeDTO> list() {
        Map<Integer, List<TemplateDTO>> folderTemplatesMap = templateService.findAll()
                .stream()
                .map(templateMapper::shortMap)
                .collect(groupingBy(TemplateDTO::getFolderId));

        Map<Integer, FolderDTO> folders = folderService.findAllTemplateFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

        return templateMapper.joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public TemplateDTO get(Integer id) {
        return templateMapper.map(templateService.findById(id));
    }


    public PageableResponse<TemplateDTO> search(SearchRequest request) {
        Page<TemplateEntity> page = templateService.searchByName(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(templateMapper::shortMap)
                        .collect(toList())
        );
    }

    @Transactional
    public void delete(Integer id) {
        checkUsability(id);
        templateService.delete(templateService.findById(id));
    }

    private void checkUsability(Integer id) {
        if (templateService.isUsed(id)) throw new UnmodifiedException("Template already is used");
    }
}

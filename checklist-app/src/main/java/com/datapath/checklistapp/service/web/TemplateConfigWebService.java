package com.datapath.checklistapp.service.web;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.service.*;
import com.datapath.checklistapp.dao.service.classifier.TemplateConfigTypeDaoService;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.SaveTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.SaveTemplateConfigRequest.BaseQuestion;
import com.datapath.checklistapp.dto.request.template.SaveTemplateConfigRequest.TemplateConfigQuestion;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.exception.UnmodifiedException;
import com.datapath.checklistapp.service.mapper.QuestionMapper;
import com.datapath.checklistapp.service.mapper.TemplateMapper;
import com.datapath.checklistapp.service.validation.ValidateService;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.QuestionExecutionRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.datapath.checklistapp.util.Constants.ACTIVITY_TEMPLATE_TYPE;
import static com.datapath.checklistapp.util.database.QuestionExecutionRole.*;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class TemplateConfigWebService {

    private final TemplateConfigDaoService templateConfigService;
    private final UserDaoService userService;
    private final FolderDaoService folderService;
    private final QuestionDaoService questionService;
    private final TemplateConfigTypeDaoService templateTypeService;
    private final TemplateMapper templateMapper;
    private final QuestionMapper questionMapper;
    private final QuestionExecutionDaoService questionExecutionService;
    private final ValidateService validateService;

    @Transactional
    public void create(SaveTemplateConfigRequest request) {
        TemplateConfigEntity entity = new TemplateConfigEntity();

        if (nonNull(request.getId())) {
            checkUsability(request.getId());
            entity.setId(request.getId());
        }

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        entity.setType(templateTypeService.findById(request.getTemplateConfigTypeId()));
        entity.setFolder(folderService.findTemplateConfigFolderById(request.getFolderId()));

        entity.getQuestions().add(handleQuestion(request.getObjectQuestion(), OBJECT));
        entity.getQuestions().addAll(handleQuestions(request.getObjectFeatureQuestions(), OBJECT_FUTURE));

        entity.getQuestions().addAll(handleQuestions(request.getTypeQuestions(), TYPE));

        if (ACTIVITY_TEMPLATE_TYPE.equals(request.getTemplateConfigTypeId())) {
            entity.getQuestions().add(handleQuestion(request.getAuthorityQuestion(), AUTHORITY));
            entity.getQuestions().addAll(handleQuestions(request.getAuthorityFeatureQuestions(), AUTHORITY_FEATURE));
        }

        templateConfigService.save(entity);
    }

    private QuestionExecutionEntity handleQuestion(BaseQuestion question, QuestionExecutionRole role) {
        QuestionEntity qEntity = questionService.findById(question.getQuestionId());

        validateService.validate(qEntity);

        return questionExecutionService.save(
                questionMapper.map(question.asTemplateQuestion(), qEntity, role)
        );
    }

    private List<QuestionExecutionEntity> handleQuestions(List<TemplateConfigQuestion> questions, QuestionExecutionRole role) {
        if (isEmpty(questions)) return emptyList();

        List<QuestionExecutionEntity> entities = new ArrayList<>();

        List<TemplateConfigQuestion> roots = questions.stream()
                .filter(q -> isNull(q.getParentHash()))
                .collect(toList());

        processQuestions(roots, questions, entities, role, null);

        return entities;
    }

    private void processQuestions(List<TemplateConfigQuestion> dtos,
                                  List<TemplateConfigQuestion> allDtos,
                                  List<QuestionExecutionEntity> entities,
                                  QuestionExecutionRole role,
                                  Integer parentQuestionId) {
        dtos.forEach(q -> {
            QuestionEntity qEntity = questionService.findById(q.getQuestionId());
            QuestionExecutionEntity qexEntity = questionMapper.map(q, qEntity, role);

            if (nonNull(parentQuestionId)) {
                qexEntity.setParentQuestionId(parentQuestionId);
            } else {
                qexEntity.setRoot(true);
            }

            QuestionExecutionEntity saved = questionExecutionService.save(qexEntity);
            entities.add(saved);

            List<TemplateConfigQuestion> children = getByParentHash(allDtos, q.getHash());
            if (!isEmpty(children)) processQuestions(children, allDtos, entities, role, saved.getId());
        });
    }

    private List<TemplateConfigQuestion> getByParentHash(List<TemplateConfigQuestion> questions,
                                                         String parentHash) {
        return questions.stream()
                .filter(q -> parentHash.equals(q.getParentHash()))
                .collect(toList());
    }


    public List<TemplateFolderTreeDTO> list(Integer templateType) {
        List<TemplateConfigEntity> entities;

        if (nonNull(templateType)) {
            entities = templateConfigService.findAllByType(templateTypeService.findById(templateType));
        } else {
            entities = templateConfigService.findAll();
        }

        Map<Integer, List<TemplateDTO>> folderTemplatesMap = entities.stream()
                .map(templateMapper::shortMap)
                .collect(groupingBy(TemplateDTO::getFolderId));

        Map<Integer, FolderDTO> folders = folderService.findAllTemplateConfigFolders()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, identity()));

        return templateMapper.joinFolderWithTemplates(folderTemplatesMap, folders);
    }

    public TemplateDTO get(Integer id) {
        return templateMapper.map(templateConfigService.findById(id));
    }

    public PageableResponse<TemplateDTO> search(SearchRequest request) {
        Page<TemplateConfigEntity> page = templateConfigService.searchByName(request);

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
        templateConfigService.delete(templateConfigService.findById(id));
    }

    private void checkUsability(Integer id) {
        if (templateConfigService.isUsed(id)) throw new UnmodifiedException("Template config already is used");
    }
}

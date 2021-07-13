package com.datapath.checklistapp.service.web;

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
import com.datapath.checklistapp.service.mapper.QuestionMapper;
import com.datapath.checklistapp.service.mapper.TemplateMapper;
import com.datapath.checklistapp.util.UserUtils;
import com.datapath.checklistapp.util.database.QuestionExecutionRole;
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

import static com.datapath.checklistapp.util.Constants.ACTIVITY_TEMPLATE_TYPE;
import static java.util.Objects.nonNull;
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

    @Transactional
    public void create(CreateTemplateConfigRequest request) {
        validateQuestions(request);

        TemplateConfigEntity entity = new TemplateConfigEntity();

        entity.setName(request.getName());
        entity.setAuthor(userService.findById(UserUtils.getCurrentUserId()));
        entity.setType(templateTypeService.findById(request.getTemplateConfigTypeId()));
        entity.setFolder(folderService.findTemplateConfigFolderById(request.getFolderId()));

        QuestionExecutionEntity objectQuestionExecution = questionExecutionService.save(
                questionMapper.map(
                        request.getObjectQuestion().asTemplateQuestion(),
                        questionService.findById(request.getObjectQuestion().getQuestionId()),
                        QuestionExecutionRole.OBJECT)
        );
        entity.getQuestions().add(objectQuestionExecution);

        Set<QuestionExecutionEntity> objectFeatureQuestions = new HashSet<>();
        request.getObjectFeatureQuestions().forEach(
                q -> processQuestion(q, questionService.findById(q.getQuestionId()), objectFeatureQuestions, null, QuestionExecutionRole.OBJECT_FUTURE)
        );
        entity.getQuestions().addAll(objectFeatureQuestions);

        if (nonNull(request.getAuthorityQuestion())) {
            QuestionExecutionEntity authorityQuestionExecution = questionExecutionService.save(
                    questionMapper.map(
                            request.getAuthorityQuestion().asTemplateQuestion(),
                            questionService.findById(request.getAuthorityQuestion().getQuestionId()),
                            QuestionExecutionRole.AUTHORITY
                    )
            );
            entity.getQuestions().add(authorityQuestionExecution);

            Set<QuestionExecutionEntity> authorityFeatureQuestions = new HashSet<>();
            request.getAuthorityFeatureQuestions().forEach(
                    q -> processQuestion(q, questionService.findById(q.getQuestionId()), authorityFeatureQuestions, null, QuestionExecutionRole.AUTHORITY_FEATURE)
            );
            entity.getQuestions().addAll(authorityFeatureQuestions);
        }

        Set<QuestionExecutionEntity> typeQuestions = new HashSet<>();
        request.getTypeQuestions().forEach(
                q -> processQuestion(q, questionService.findById(q.getQuestionId()), typeQuestions, null, QuestionExecutionRole.TYPE)
        );
        entity.getQuestions().addAll(typeQuestions);

        templateConfigService.save(entity);
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
                }).collect(toMap(FolderDTO::getId, Function.identity()));

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
        if (templateConfigService.isUsed(id)) throw new UnmodifiedException("Template config already is used");
        templateConfigService.delete(templateConfigService.findById(id));
    }

    private void processQuestion(CreateTemplateConfigRequest.TemplateConfigQuestion dtoQuestion,
                                 QuestionEntity daoQuestion,
                                 Set<QuestionExecutionEntity> questionExecutions,
                                 QuestionExecutionEntity parentQuestion,
                                 QuestionExecutionRole role) {
        QuestionExecutionEntity question = questionMapper.map(dtoQuestion, daoQuestion, role);

        if (nonNull(parentQuestion)) {
            question.setParentQuestionId(parentQuestion.getId());
        } else {
            question.setRoot(true);
        }

        QuestionExecutionEntity saved = questionExecutionService.save(question);
        questionExecutions.add(saved);

        if (!isEmpty(dtoQuestion.getSubQuestions())) {
            dtoQuestion.getSubQuestions()
                    .forEach(q -> processQuestion(q, questionService.findById(q.getQuestionId()), questionExecutions, saved, role));
        }
    }

    private void validateQuestions(CreateTemplateConfigRequest request) {
        QuestionEntity objectQuestion = questionService.findById(request.getObjectQuestion().getQuestionId());

        if (!hasIdentifier(objectQuestion))
            throw new ValidationException("Invalid base question. Answer structure must has identifier fields");

        if (ACTIVITY_TEMPLATE_TYPE.equals(request.getTemplateConfigTypeId())) {
            QuestionEntity authorityQuestion = questionService.findById(request.getAuthorityQuestion().getQuestionId());

            if (!hasIdentifier(authorityQuestion))
                throw new ValidationException("Invalid authority representative question. Answer structure must has identifier fields");
        }
    }

    private boolean hasIdentifier(QuestionEntity question) {
        return question.getAnswerStructure().getFields().stream()
                .anyMatch(FieldDescriptionEntity::isIdentifier);
    }
}

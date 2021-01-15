package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import com.datapath.checklistukraineapp.dao.service.QuestionDaoService;
import com.datapath.checklistukraineapp.dao.service.TemplateDaoService;
import com.datapath.checklistukraineapp.dao.service.TemplateFolderDaoService;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistukraineapp.dto.TemplateQuestionDTO;
import com.datapath.checklistukraineapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistukraineapp.dto.response.TemplateResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.checklistukraineapp.util.UserUtils.getCurrentUserId;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class TemplateWebService {

    private final TemplateDaoService templateService;
    private final UserDaoService userService;
    private final TemplateFolderDaoService templateFolderService;
    private final QuestionDaoService questionService;

    @Transactional
    public void create(CreateTemplateRequest request) {
        UserEntity author = userService.findById(getCurrentUserId());
        TemplateFolderEntity folder = templateFolderService.findById(request.getFolderId());

        TemplateEntity entity = new TemplateEntity();

        entity.setName(request.getName());
        entity.setAuthor(author);
        entity.setFolder(folder);

        request.getQuestions().forEach(q -> {
            QuestionEntity question = questionService.findById(q.getQuestionId());

            TemplateQuestionRelationship relationship = new TemplateQuestionRelationship();
            relationship.setGroupName(q.getGroupName());
            relationship.setQuestion(question);

            if (nonNull(q.getParentQuestionId())) {
                QuestionEntity parentQuestion = questionService.findById(q.getParentQuestionId());
                relationship.setParentQuestionId(parentQuestion.getId());
                relationship.setAnswerId(q.getParentAnswerId());
            }

            entity.getQuestions().add(relationship);
        });

        templateService.save(entity);
    }

    public List<TemplateFolderTreeDTO> list() {
        Map<Long, FolderDTO> folders = templateFolderService.findAll()
                .stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                }).collect(toMap(FolderDTO::getId, Function.identity()));

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
    public TemplateResponse get(Long id) {
        TemplateResponse response = new TemplateResponse();

        TemplateEntity entity = templateService.findById(id);

        TemplateDTO template = new TemplateDTO();
        BeanUtils.copyProperties(entity, template);

        response.setTemplate(template);

        response.setQuestions(
                entity.getQuestions().stream()
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
        );

        return response;
    }
}

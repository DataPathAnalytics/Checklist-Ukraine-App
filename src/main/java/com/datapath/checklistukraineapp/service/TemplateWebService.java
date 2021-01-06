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
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.TemplateQuestionDTO;
import com.datapath.checklistukraineapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistukraineapp.dto.response.TemplateResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.datapath.checklistukraineapp.util.UserUtils.getCurrentUserId;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class TemplateWebService {

    private final TemplateDaoService templateService;
    private final UserDaoService userService;
    private final TemplateFolderDaoService templateFolderService;
    private final QuestionDaoService questionService;

    @Transactional
    public void save(CreateTemplateRequest request) {
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
                relationship.setAnswerId(q.getAnswerId());
            }

            entity.getQuestions().add(relationship);
        });

        templateService.save(entity);
    }

    public List<TemplateDTO> list() {
        return templateService.findAll().stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    return dto;
                })
                .collect(toList());
    }

    public TemplateResponse get(Long id) {
        TemplateResponse response = new TemplateResponse();

        TemplateEntity entity = templateService.findById(id);

        TemplateDTO template = new TemplateDTO();
        BeanUtils.copyProperties(entity, template);
        template.setAuthorId(entity.getAuthor().getId());
        template.setFolderId(entity.getFolder().getId());

        response.setTemplate(template);

        response.setQuestions(
                entity.getQuestions().stream()
                        .map(q -> {
                            TemplateQuestionDTO dto = new TemplateQuestionDTO();

                            dto.setQuestionId(q.getQuestion().getId());
                            dto.setGroupName(q.getGroupName());
                            dto.setParentQuestionId(q.getParentQuestionId());
                            dto.setAnswerId(q.getAnswerId());

                            return dto;
                        }).collect(Collectors.groupingBy(TemplateQuestionDTO::getGroupName))
        );

        return response;
    }
}

package com.datapath.checklistukraineapp.util;

import com.datapath.checklistukraineapp.dao.entity.*;
import com.datapath.checklistukraineapp.dao.relatioship.TemplateQuestionRelationship;
import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistPageResponse;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistResponse;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT;
import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class DtoEntityConverter {

    public static ChecklistDTO map(ResponseSessionEntity entity) {
        ChecklistDTO dto = new ChecklistDTO();

        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setTemplateId(entity.getTemplate().getId());
        dto.setTemplateName(entity.getTemplate().getName());
        dto.setChecklistStatusId(entity.getStatus().getSessionStatusId());

        if (nonNull(entity.getReviewer())) {
            dto.setReviewerId(entity.getReviewer().getId());
        }

        return dto;
    }

    public static ChecklistResponse mapFullResponse(ResponseSessionEntity entity) {
        ChecklistResponse response = new ChecklistResponse();

        BeanUtils.copyProperties(entity, response);
        response.setAuthorId(entity.getAuthor().getId());
        response.setTemplateId(entity.getTemplate().getId());
        response.setChecklistStatusId(entity.getStatus().getSessionStatusId());

        if (nonNull(entity.getReviewer())) {
            response.setReviewerId(entity.getReviewer().getId());
        }

        Map<Long, TemplateQuestionRelationship> questionIdMap = entity.getTemplate()
                .getFactQuestions()
                .stream()
                .collect(Collectors.toMap(t -> t.getQuestion().getId(), Function.identity()));

        Map<Long, AnswerEntity> questionIdAnswerMap = entity.getAnswers()
                .stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), Function.identity()));


//        response.setQuestionAnswerList(
//                entity.getAnswers().stream()
//                        .map()
//        );

        return response;
    }

    public static ControlEventDTO map(ControlActivityEntity event) {
        ControlEventDTO dto = new ControlEventDTO();

        BeanUtils.copyProperties(event, dto);
        dto.setMembers(
                event.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
        dto.setAuthorId(event.getAuthor().getId());
        dto.setControlStatusId(event.getStatus().getActivityStatusId());
//        dto.setControlObjectName(event.getObject().getName());
//        dto.setControlObjectId(event.getObject().getControlObjectId());
        dto.setControlTypeId(event.getType().getAuthorityId());
        dto.setTemplates(
                event.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        ChecklistPageResponse checklistPage = new ChecklistPageResponse();
        checklistPage.setPageSize(DEFAULT_EVENT_CHECKLIST_COUNT);
        checklistPage.setCurrentPage(DEFAULT_EVENT_CHECKLIST_PAGE);
        checklistPage.setTotalCount(event.getChecklists().size());
        checklistPage.setTotalPageCount((int) Math.ceil((double) event.getChecklists().size() / DEFAULT_EVENT_CHECKLIST_COUNT));
        checklistPage.setChecklists(
                event.getChecklists().stream()
                        .limit(DEFAULT_EVENT_CHECKLIST_COUNT)
                        .sorted(Comparator.comparing(ResponseSessionEntity::getDateCreated)
                                .thenComparing(ResponseSessionEntity::getName))
                        .map(DtoEntityConverter::map)
                        .collect(toList())
        );

        dto.setChecklists(checklistPage);

        return dto;
    }

    public static QuestionDTO map(QuestionEntity entity) {
        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setKnowledgeCategoryId(entity.getKnowledgeCategory().getKnowledgeCategoryId());
        dto.setKnowledgeCategoryValue(entity.getKnowledgeCategory().getValue());
        dto.setKnowledgeCategoryTranslate(entity.getKnowledgeCategory().getTranslate());

        dto.setAnswerStructureId(nonNull(entity.getAnswerStructure()) ? entity.getAnswerStructure().getId() : null);
        dto.setQuestionTypeId(entity.getType().getQuestionTypeId());
        dto.setQuestionTypeValue(entity.getType().getValue());

        dto.setQuestionSourceId(entity.getSource().getSource().getId());
        dto.setQuestionSourceName(entity.getSource().getSource().getName());
        dto.setQuestionSourceLink(entity.getSource().getSource().getLink());
        dto.setDocumentParagraph(entity.getSource().getDocumentParagraph());

        return dto;
    }
}

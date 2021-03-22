package com.datapath.checklistapp.service.converter.structure;

import com.datapath.checklistapp.dao.entity.*;
import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.QuestionExecutionDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.TemplateDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT;
import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE;
import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class ControlActivityConverter {

    private final ResponseSessionConverter responseSessionConverter;
    private final QuestionConverter questionConverter;

    public ControlActivityDTO map(ControlActivityEntity activity) {
        ControlActivityDTO dto = new ControlActivityDTO();

        BeanUtils.copyProperties(activity, dto);
        dto.setMembers(
                activity.getMembers().stream()
                        .map(UserEntity::getId)
                        .collect(toSet())
        );
        dto.setAuthorId(activity.getAuthor().getId());
        dto.setStatusId(activity.getStatus().getActivityStatusId());

        dto.setTemplates(
                activity.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO templateDto = new TemplateDTO();
                            BeanUtils.copyProperties(t, templateDto);
                            templateDto.setAuthorId(t.getAuthor().getId());
                            templateDto.setFolderId(t.getFolder().getId());
                            return templateDto;
                        }).collect(toList()));

        Map<Long, AnswerEntity> answerQuestionId = activity.getActivityResponse().getAnswers()
                .stream()
                .collect(toMap(a -> a.getQuestionExecution().getId(), Function.identity()));

        List<QuestionExecutionDTO> featureQuestions = activity.getActivityResponse()
                .getTemplateConfig()
                .getFutureQuestionExecutions()
                .stream()
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> typeQuestions = activity.getActivityResponse()
                .getTemplateConfig()
                .getTypeQuestionExecutions()
                .stream()
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        List<QuestionExecutionDTO> authorityQuestions = activity.getActivityResponse()
                .getTemplateConfig()
                .getAuthorityQuestionExecutions()
                .stream()
                .map(q -> questionConverter.map(q, answerQuestionId.get(q.getId())))
                .collect(toList());

        QuestionExecutionEntity objectQuestion = activity.getActivityResponse()
                .getTemplateConfig()
                .getObjectQuestionExecution();

        dto.setObjectQuestion(questionConverter.map(objectQuestion, answerQuestionId.get(objectQuestion.getId())));
        dto.setObjectFeatureQuestions(featureQuestions);
        dto.setTypeQuestions(typeQuestions);
        dto.setAuthorityQuestions(authorityQuestions);

        SessionPageDTO sessionPage = new SessionPageDTO();
        sessionPage.setPageSize(DEFAULT_EVENT_CHECKLIST_COUNT);
        sessionPage.setCurrentPage(DEFAULT_EVENT_CHECKLIST_PAGE);
        sessionPage.setTotalCount(activity.getSessionResponses().size());
        sessionPage.setTotalPageCount((int) Math.ceil((double) activity.getSessionResponses().size() / DEFAULT_EVENT_CHECKLIST_COUNT));
        sessionPage.setSessions(
                activity.getSessionResponses().stream()
                        .limit(DEFAULT_EVENT_CHECKLIST_COUNT)
                        .sorted(Comparator.comparing(ResponseSessionEntity::getDateCreated)
                                .thenComparing(ResponseSessionEntity::getName))
                        .map(responseSessionConverter::mapResponseSessionBaseInfo)
                        .collect(toList())
        );

        dto.setSessions(sessionPage);

        return dto;
    }
}

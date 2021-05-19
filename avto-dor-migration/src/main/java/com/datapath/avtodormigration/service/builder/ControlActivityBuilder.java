package com.datapath.avtodormigration.service.builder;

import com.datapath.avtodormigration.QuestionValues;
import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dto.request.AnswerDTO;
import com.datapath.avtodormigration.dto.request.ControlActivityRequest;
import com.datapath.avtodormigration.service.TemplateProvideService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class ControlActivityBuilder implements Builder {

    private final TemplateProvideService templateService;

    public ControlActivityRequest prepareToMigration(ChecklistEntity checklist) {
        ControlActivityRequest request = new ControlActivityRequest();
        request.setTemplateConfigId(templateService.getTemplateConfigId());
        request.setTemplateIds(Collections.singletonList(templateService.getTemplateId()));

        request.getAnswers().add(prepareObjectAnswer(checklist));
        request.getAnswers().add(prepareAuthorityAnswer(checklist));
        request.getAnswers().add(prepareDasuBranchAnswer(checklist));
        request.getAnswers().add(prepareDfkPeriodAnswer(checklist));
        request.getAnswers().add(prepareFinControlTypeAnswer(checklist));

        return request;
    }

    private AnswerDTO prepareFinControlTypeAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplateConfig().getTypeQuestions(),
                QuestionValues.TYPE_QUESTION_FIN_CONTROL_TYPE);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getEvent().getId());
        answer.getValues().put("name", checklist.getEvent().getName());

        return answer;
    }

    private AnswerDTO prepareDfkPeriodAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplateConfig().getTypeQuestions(),
                QuestionValues.TYPE_QUESTION_DFK_PERIOD);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("startDate", asString(checklist.getDfkPeriodStartDate()));
        answer.getValues().put("endDate", asString(checklist.getDfkPeriodEndDate()));

        return answer;
    }

    private AnswerDTO prepareDasuBranchAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplateConfig().getAuthorityFeatureQuestions(),
                QuestionValues.AUTHORITY_FEATURE_QUESTION_DASU_BRANCH);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getOffice().getId());
        answer.getValues().put("name", checklist.getOffice().getName());

        return answer;
    }

    private AnswerDTO prepareAuthorityAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.getTemplateConfig().getAuthorityQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("id", checklist.getAuditor().getId());
        answer.getValues().put("name", checklist.getAuditor().getName());
        answer.getValues().put("email", checklist.getAuditor().getEmail());

        return answer;
    }

    private AnswerDTO prepareObjectAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.getTemplateConfig().getObjectQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getBuyer().getIdentifierId());
        answer.getValues().put("name", checklist.getBuyer().getIdentifierLegalName());

        return answer;
    }
}

package com.datapath.avtodormigration.service.builder.impl;

import com.datapath.avtodormigration.QuestionValues;
import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dto.request.AnswerDTO;
import com.datapath.avtodormigration.dto.request.ControlActivityRequest;
import com.datapath.avtodormigration.service.TemplateProvideService;
import com.datapath.avtodormigration.service.builder.ControlActivityBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.datapath.avtodormigration.Constants.COVID_19_AVTODOR_TEMPLATE_TYPE_ID;

@Service
@AllArgsConstructor
public class AvtodorCovid19ControlActivityBuilder implements ControlActivityBuilder {

    private final TemplateProvideService templateService;

    @Override
    public void prepareOtherAnswers(ControlActivityRequest request, ChecklistEntity checklist) {
        request.getAnswers().add(prepareDasuBranchAnswer(checklist));
        request.getAnswers().add(prepareDfkPeriodAnswer(checklist));
    }

    @Override
    public Integer forTemplateType() {
        return COVID_19_AVTODOR_TEMPLATE_TYPE_ID;
    }

    @Override
    public TemplateProvideService getTemplateProvideService() {
        return templateService;
    }

    private AnswerDTO prepareDfkPeriodAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplateConfig(forTemplateType()).getTypeQuestions(),
                QuestionValues.TYPE_QUESTION_DFK_PERIOD);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("startDate", asString(checklist.getDfkPeriodStartDate()));
        answer.getValues().put("endDate", asString(checklist.getDfkPeriodEndDate()));

        return answer;
    }

    private AnswerDTO prepareDasuBranchAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplateConfig(forTemplateType()).getAuthorityFeatureQuestions(),
                QuestionValues.AUTHORITY_FEATURE_QUESTION_DASU_BRANCH);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getOffice().getId());
        answer.getValues().put("name", checklist.getOffice().getName());

        return answer;
    }
}

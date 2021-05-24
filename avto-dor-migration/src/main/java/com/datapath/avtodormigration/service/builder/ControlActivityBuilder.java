package com.datapath.avtodormigration.service.builder;

import com.datapath.avtodormigration.QuestionValues;
import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dto.request.AnswerDTO;
import com.datapath.avtodormigration.dto.request.ControlActivityRequest;

import java.util.Collections;

public interface ControlActivityBuilder extends Builder {

    default void prepareOtherAnswers(ControlActivityRequest request, ChecklistEntity checklist) {
    }

    default ControlActivityRequest prepareToMigration(ChecklistEntity checklist) {
        ControlActivityRequest request = new ControlActivityRequest();
        request.setTemplateConfigId(getTemplateProvideService().getTemplateConfigId(forTemplateType()));
        request.setTemplateIds(Collections.singletonList(getTemplateProvideService().getTemplateId(forTemplateType())));

        request.getAnswers().add(prepareObjectAnswer(checklist));
        request.getAnswers().add(prepareAuthorityAnswer(checklist));
        request.getAnswers().add(prepareFinControlTypeAnswer(checklist));
        request.getAnswers().add(prepareTemplateType(checklist));

        prepareOtherAnswers(request, checklist);

        return request;
    }

    default AnswerDTO prepareTemplateType(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplateConfig(forTemplateType()).getTypeQuestions(),
                QuestionValues.TYPE_QUESTION_TEMPLATE_TYPE);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getTemplateTypeId());
        answer.getValues().put("name", checklist.getTemplateName());

        return answer;
    }

    default AnswerDTO prepareFinControlTypeAnswer(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplateConfig(forTemplateType()).getTypeQuestions(),
                QuestionValues.TYPE_QUESTION_FIN_CONTROL_TYPE);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getEvent().getId());
        answer.getValues().put("name", checklist.getEvent().getName());

        return answer;
    }

    default AnswerDTO prepareAuthorityAnswer(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().getTemplateConfig(forTemplateType()).getAuthorityQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("id", checklist.getAuditor().getId());
        answer.getValues().put("name", checklist.getAuditor().getName());
        answer.getValues().put("email", checklist.getAuditor().getEmail());

        return answer;
    }

    default AnswerDTO prepareObjectAnswer(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().getTemplateConfig(forTemplateType()).getObjectQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("identifier", checklist.getBuyer().getIdentifierId());
        answer.getValues().put("name", checklist.getBuyer().getIdentifierLegalName());

        return answer;
    }
}

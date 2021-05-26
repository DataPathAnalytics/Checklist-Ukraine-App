package com.datapath.dasuchecklistmigration.service.builder;

import com.datapath.dasuchecklistmigration.dao.entity.ChecklistEntity;
import com.datapath.dasuchecklistmigration.dao.entity.ContractEntity;
import com.datapath.dasuchecklistmigration.dao.service.DaoService;
import com.datapath.dasuchecklistmigration.dto.request.AnswerDTO;
import com.datapath.dasuchecklistmigration.dto.request.ResponseSessionRequest;

import java.util.List;

import static com.datapath.dasuchecklistmigration.QuestionValues.*;

public interface ResponseSessionBuilder extends Builder {

    DaoService gteDaoService();

    List<AnswerDTO> prepareAnswersFromQuestionGroups(ChecklistEntity checklist);

    default ResponseSessionRequest prepareToMigration(ChecklistEntity checklist, ContractEntity contract, Integer controlActivityId) {
        ResponseSessionRequest request = new ResponseSessionRequest();
        request.setControlActivityId(controlActivityId);
        request.setTemplateId(getTemplateProvideService().getTemplateId(forTemplateType()));

        request.getAnswers().add(prepareObjectAnswer(contract));
        request.getAnswers().add(prepareCpvAnswer(contract));
        request.getAnswers().add(prepareStartDateAnswer(checklist));
        request.getAnswers().add(prepareSummaryAnswer(checklist));

        prepareOtherAnswers(request, contract, checklist);

        request.getAnswers().addAll(prepareAnswersFromQuestionGroups(checklist));

        return request;
    }

    default void prepareOtherAnswers(ResponseSessionRequest request, ContractEntity contract, ChecklistEntity checklist) {
    }

    default AnswerDTO prepareCpvAnswer(ContractEntity contract) {
        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplate(forTemplateType()).getObjectFeatureQuestions(),
                OBJECT_FEATURE_CPV);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        String cpvCode = gteDaoService().getCpvCodeByContractId(contract.getId());
        String cpvName = gteDaoService().getCpvNameByCpvCode(cpvCode);
        answer.getValues().put("identifier", cpvCode);
        answer.getValues().put("name", cpvName);

        return answer;
    }

    default AnswerDTO prepareSummaryAnswer(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplate(forTemplateType()).getTypeQuestions(),
                TYPE_QUESTION_SUMMARY);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("value", checklist.getSummary());

        return answer;
    }

    default AnswerDTO prepareStartDateAnswer(ChecklistEntity checklist) {
        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplate(forTemplateType()).getTypeQuestions(),
                TYPE_QUESTION_START_DATE);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("value", asString(checklist.getStartDate()));

        return answer;
    }

    default AnswerDTO prepareObjectAnswer(ContractEntity contract) {
        Integer questionId = getTemplateProvideService().getTemplate(forTemplateType()).getObjectQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("contractNumber", contract.getContractId());
        answer.getValues().put("amount", contract.getValueAmount());
        answer.getValues().put("currency", contract.getCurrency());
        answer.getValues().put("startDate", asString(contract.getStartDate()));
        answer.getValues().put("endDate", asString(contract.getEndDate()));

        return answer;
    }
}

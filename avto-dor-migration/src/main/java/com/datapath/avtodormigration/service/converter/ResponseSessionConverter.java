package com.datapath.avtodormigration.service.converter;

import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dao.entity.ContractEntity;
import com.datapath.avtodormigration.dto.request.AnswerDTO;
import com.datapath.avtodormigration.dto.request.ResponseSessionRequest;
import com.datapath.avtodormigration.service.TemplateProvideService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.datapath.avtodormigration.QuestionValues.*;

@Service
@AllArgsConstructor
public class ResponseSessionConverter {

    private final TemplateProvideService templateService;


    public ResponseSessionRequest prepareToMigration(ChecklistEntity checklist,
                                                     ContractEntity contract,
                                                     Integer controlActivityId) {
        ResponseSessionRequest request = new ResponseSessionRequest();
        request.setControlActivityId(controlActivityId);
        request.setTemplateId(templateService.getTemplateId());

        request.getAnswers().add(prepareObjectAnswer(contract));
//        request.getAnswers().add(prepareCpvAnswer());
        request.getAnswers().add(prepareRevisionPeriodAnswer(checklist));
        request.getAnswers().add(prepareStartDateAnswer(checklist));
        request.getAnswers().add(prepareSummaryAnswer(checklist));
//        request.getAnswers().add();

        return request;
    }

    private AnswerDTO prepareSummaryAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplate().getTypeQuestions(),
                TYPE_QUESTION_SUMMARY);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("value", checklist.getSummary());

        return answer;
    }

    private AnswerDTO prepareStartDateAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplate().getTypeQuestions(),
                TYPE_QUESTION_START_DATE);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("value", checklist.getStartDate());

        return answer;
    }

    private AnswerDTO prepareRevisionPeriodAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplate().getTypeQuestions(),
                TYPE_QUESTION_REVISION_PERIOD);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("startDate", checklist.getRevisionPeriodStartDate());
        answer.getValues().put("endDate", checklist.getRevisionPeriodEndDate());

        return answer;
    }

    private AnswerDTO prepareObjectAnswer(ContractEntity contract) {
        Integer questionId = templateService.getTemplate().getObjectQuestion().getId();

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("contractNumber", contract.getContractId());
        answer.getValues().put("amount", contract.getAmount());
        answer.getValues().put("currency", contract.getCurrency());
        answer.getValues().put("startDate", contract.getStartDate().toLocalDate());
        answer.getValues().put("endDate", contract.getEndDate().toLocalDate());

        return answer;
    }
}

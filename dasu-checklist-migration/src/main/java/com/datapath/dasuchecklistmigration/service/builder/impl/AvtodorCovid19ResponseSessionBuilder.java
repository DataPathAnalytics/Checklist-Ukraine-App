package com.datapath.dasuchecklistmigration.service.builder.impl;

import com.datapath.dasuchecklistmigration.dao.entity.AnswerEntity;
import com.datapath.dasuchecklistmigration.dao.entity.ChecklistAmountInfoEntity;
import com.datapath.dasuchecklistmigration.dao.entity.ChecklistEntity;
import com.datapath.dasuchecklistmigration.dao.entity.ContractEntity;
import com.datapath.dasuchecklistmigration.dao.service.DaoService;
import com.datapath.dasuchecklistmigration.dto.request.AnswerDTO;
import com.datapath.dasuchecklistmigration.dto.request.ResponseSessionRequest;
import com.datapath.dasuchecklistmigration.dto.response.GroupQuestionsDTO;
import com.datapath.dasuchecklistmigration.dto.response.QuestionExecutionDTO;
import com.datapath.dasuchecklistmigration.service.TemplateProvideService;
import com.datapath.dasuchecklistmigration.service.builder.ResponseSessionBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.datapath.dasuchecklistmigration.Constants.COVID_19_AVTODOR_TEMPLATE_TYPE_ID;
import static com.datapath.dasuchecklistmigration.QuestionValues.*;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class AvtodorCovid19ResponseSessionBuilder implements ResponseSessionBuilder {

    private final TemplateProvideService templateService;
    private final DaoService daoService;

    @Override
    public void prepareOtherAnswers(ResponseSessionRequest request, ContractEntity contract, ChecklistEntity checklist) {
        request.getAnswers().add(prepareRevisionPeriodAnswer(checklist));
        request.getAnswers().addAll(prepareAmountAnswers(checklist));
    }

    @Override
    public DaoService gteDaoService() {
        return daoService;
    }

    @Override
    public Integer forTemplateType() {
        return COVID_19_AVTODOR_TEMPLATE_TYPE_ID;
    }

    @Override
    public TemplateProvideService getTemplateProvideService() {
        return templateService;
    }

    @Override
    public List<AnswerDTO> prepareAnswersFromQuestionGroups(ChecklistEntity checklist) {
        List<AnswerDTO> answers = new ArrayList<>();
        templateService.getTemplate(forTemplateType()).getQuestionGroups()
                .forEach(group -> answers.addAll(prepareAnswersFromQuestionGroup(group, checklist)));
        return answers;
    }

    private List<AnswerDTO> prepareAnswersFromQuestionGroup(GroupQuestionsDTO group, ChecklistEntity checklist) {
        List<AnswerDTO> answers = new ArrayList<>();

        List<AnswerEntity> checklistAnswers = checklist.getAnswers()
                .stream()
                .filter(a -> group.getGroupName().equals(a.getCategoryName()))
                .collect(toList());

        if (isEmpty(checklistAnswers)) return Collections.emptyList();

        group.getQuestions().stream()
                .filter(QuestionExecutionDTO::isRoot)
                .flatMap(q -> prepareRootQuestionAnswerWithChildAnswers(q, checklistAnswers).stream())
                .forEach(answers::add);

        return answers;
    }

    private List<AnswerDTO> prepareRootQuestionAnswerWithChildAnswers(QuestionExecutionDTO q, List<AnswerEntity> checklistAnswers) {
        List<AnswerDTO> answers = new ArrayList<>();

        AnswerEntity checklistAnswer = checklistAnswers.stream()
                .filter(a -> q.getQuestion().getValue().equals(a.getQuestionDescription()))
                .findFirst()
                .orElse(null);

        if (isNull(checklistAnswer) || isNull(checklistAnswer.getAnswerType())) return answers;

        Integer answerValue = checklistAnswer.getAnswerType().getId();

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setQuestionId(q.getId());
        answerDTO.getValues().put("value", answerValue);
        answerDTO.setComment(checklistAnswer.getComment());
        answers.add(answerDTO);

        q.getSubQuestions()
                .forEach(sub -> {
                    List<Integer> subQuestionsByAnswerType = sub.getConditionAnswerQuestionIds().get(answerValue.toString());
                    if (!isEmpty(subQuestionsByAnswerType))
                        subQuestionsByAnswerType.forEach(subId -> prepareAnswerFromSubQuestion(subId, q, checklistAnswer, answers));
                });

        return answers;
    }

    private void prepareAnswerFromSubQuestion(Integer questionId, QuestionExecutionDTO parentQuestion, AnswerEntity checklistAnswer, List<AnswerDTO> answers) {
        QuestionExecutionDTO execution = templateService.extractQuestionById(questionId, forTemplateType());
        Double answerValue = null;
        if (VIOLATION_AMOUNT.equals(execution.getQuestion().getValue()))
            answerValue = checklistAnswer.getViolationAmount();
        else if (LOST_AMOUNT.equals(execution.getQuestion().getValue()))
            answerValue = checklistAnswer.getLossAmount();
        else if (FIXED_AMOUNT.equals(execution.getQuestion().getValue()))
            answerValue = checklistAnswer.getEliminatedAmount();
        else if (COVID_19_AMOUNT.equals(execution.getQuestion().getValue())) {
            if (VIOLATION_AMOUNT.equals(parentQuestion.getQuestion().getValue()))
                answerValue = checklistAnswer.getViolationAmountCovidFund();
            else if (LOST_AMOUNT.equals(parentQuestion.getQuestion().getValue()))
                answerValue = checklistAnswer.getLossAmountCovidFund();
            else if (FIXED_AMOUNT.equals(parentQuestion.getQuestion().getValue()))
                answerValue = checklistAnswer.getEliminatedAmountCovidFund();
        }

        if (isNull(answerValue)) return;

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setQuestionId(execution.getId());
        answerDTO.getValues().put("amount", answerValue);
        answerDTO.getValues().put("currency", "UAH");
        answers.add(answerDTO);

        execution.getSubQuestions()
                .forEach(sub -> sub.getQuestionIds()
                        .forEach(subId -> prepareAnswerFromSubQuestion(subId, execution, checklistAnswer, answers)));
    }

    private List<AnswerDTO> prepareAmountAnswers(ChecklistEntity checklist) {
        List<AnswerDTO> answers = new ArrayList<>();
        ChecklistAmountInfoEntity amountInfo = checklist.getAmountInfo();

        prepareAnswerByQuestionValueAndValue(answers, TOTAL_CONTRACT_AMOUNT_STATE_BUDGET, amountInfo.getTotalContractAmountStateBudget());
        prepareAnswerByQuestionValueAndValue(answers, REVISION_PERIOD_TOTAL_CONTRACT_AMOUNT_STATE_BUDGET, amountInfo.getRevisionPeriodTotalContractAmountStateBudget());
        prepareAnswerByQuestionValueAndValue(answers, BUDGET_AMOUNT_STATE_BUDGET, amountInfo.getBudgetAmountStateBudget());
        prepareAnswerByQuestionValueAndValue(answers, COMPLETED_WORK_AMOUNT_STATE_BUDGET, amountInfo.getCompletedWorkAmountStateBudget());
        prepareAnswerByQuestionValueAndValue(answers, CHECKED_WORK_AMOUNT_STATE_BUDGET, amountInfo.getCheckedWorkAmountStateBudget());

        prepareAnswerByQuestionValueAndValue(answers, TOTAL_CONTRACT_AMOUNT_COVID_FUND, amountInfo.getTotalContractAmountCovidFund());
        prepareAnswerByQuestionValueAndValue(answers, REVISION_PERIOD_TOTAL_CONTRACT_AMOUNT_COVID_FUND, amountInfo.getRevisionPeriodTotalContractAmountCovidFund());
        prepareAnswerByQuestionValueAndValue(answers, BUDGET_AMOUNT_COVID_FUND, amountInfo.getBudgetAmountCovidFund());
        prepareAnswerByQuestionValueAndValue(answers, COMPLETED_WORK_AMOUNT_COVID_FUND, amountInfo.getCompletedWorkAmountCovidFund());
        prepareAnswerByQuestionValueAndValue(answers, CHECKED_WORK_AMOUNT_COVID_FUND, amountInfo.getCheckedWorkAmountCovidFund());

        prepareAnswerByQuestionValueAndValue(answers, TOTAL_CONTRACT_AMOUNT_LOCAL_BUDGET, amountInfo.getTotalContractAmountLocalBudget());
        prepareAnswerByQuestionValueAndValue(answers, REVISION_PERIOD_TOTAL_CONTRACT_AMOUNT_LOCAL_BUDGET, amountInfo.getRevisionPeriodTotalContractAmountLocalBudget());
        prepareAnswerByQuestionValueAndValue(answers, BUDGET_AMOUNT_LOCAL_BUDGET, amountInfo.getBudgetAmountLocalBudget());
        prepareAnswerByQuestionValueAndValue(answers, COMPLETED_WORK_AMOUNT_LOCAL_BUDGET, amountInfo.getCompletedWorkAmountLocalBudget());
        prepareAnswerByQuestionValueAndValue(answers, CHECKED_WORK_AMOUNT_LOCAL_BUDGET, amountInfo.getCheckedWorkAmountLocalBudget());

        prepareAnswerByQuestionValueAndValue(answers, TOTAL_CONTRACT_AMOUNT_OTHERS, amountInfo.getTotalContractAmountOthers());
        prepareAnswerByQuestionValueAndValue(answers, REVISION_PERIOD_TOTAL_CONTRACT_AMOUNT_OTHERS, amountInfo.getRevisionPeriodTotalContractAmountOthers());
        prepareAnswerByQuestionValueAndValue(answers, BUDGET_AMOUNT_OTHERS, amountInfo.getBudgetAmountOthers());
        prepareAnswerByQuestionValueAndValue(answers, COMPLETED_WORK_AMOUNT_OTHERS, amountInfo.getCompletedWorkAmountOthers());
        prepareAnswerByQuestionValueAndValue(answers, CHECKED_WORK_AMOUNT_OTHERS, amountInfo.getCheckedWorkAmountOthers());
        return answers;
    }

    private void prepareAnswerByQuestionValueAndValue(List<AnswerDTO> answers, String questionValue, Double answerValue) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplate(forTemplateType()).getUngroupedQuestions(),
                questionValue);

        if (isNull(answerValue)) return;

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("amount", answerValue);
        answer.getValues().put("currency", "UAH");

        answers.add(answer);
    }

    private AnswerDTO prepareRevisionPeriodAnswer(ChecklistEntity checklist) {
        Integer questionId = templateService.extractQuestionIdByValue(
                templateService.getTemplate(forTemplateType()).getTypeQuestions(),
                TYPE_QUESTION_REVISION_PERIOD);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("startDate", asString(checklist.getRevisionPeriodStartDate()));
        answer.getValues().put("endDate", asString(checklist.getRevisionPeriodEndDate()));

        return answer;
    }
}

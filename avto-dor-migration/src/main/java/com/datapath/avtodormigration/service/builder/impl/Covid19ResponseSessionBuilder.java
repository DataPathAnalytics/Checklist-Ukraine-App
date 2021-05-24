package com.datapath.avtodormigration.service.builder.impl;

import com.datapath.avtodormigration.dao.entity.AnswerEntity;
import com.datapath.avtodormigration.dao.entity.ChecklistEntity;
import com.datapath.avtodormigration.dao.entity.ContractEntity;
import com.datapath.avtodormigration.dao.service.DaoService;
import com.datapath.avtodormigration.dto.request.AnswerDTO;
import com.datapath.avtodormigration.dto.request.ResponseSessionRequest;
import com.datapath.avtodormigration.dto.response.GroupQuestionsDTO;
import com.datapath.avtodormigration.dto.response.QuestionExecutionDTO;
import com.datapath.avtodormigration.service.TemplateProvideService;
import com.datapath.avtodormigration.service.builder.ResponseSessionBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.datapath.avtodormigration.Constants.COVID_19_AVTODOR_TEMPLATE_TYPE_ID;
import static com.datapath.avtodormigration.QuestionValues.OBJECT_FEATURE_COVID_19_USED_AMOUNT;
import static com.datapath.avtodormigration.QuestionValues.OBJECT_FEATURE_USED_AMOUNT;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class Covid19ResponseSessionBuilder implements ResponseSessionBuilder {

    private final TemplateProvideService templateService;
    private final DaoService daoService;

    @Override
    public void prepareOtherAnswers(ResponseSessionRequest request, ContractEntity contract, ChecklistEntity checklist) {
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

    private List<AnswerDTO> prepareAmountAnswers(ChecklistEntity checklist) {
        List<AnswerDTO> answers = new ArrayList<>();

        if (isNull(checklist.getUsedAmount())) return answers;

        Integer questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplate(forTemplateType()).getObjectFeatureQuestions(),
                OBJECT_FEATURE_USED_AMOUNT);

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("amount", checklist.getUsedAmount());
        answer.getValues().put("currency", "UAH");

        answers.add(answer);

        if (isNull(checklist.getCovidAmount())) return answers;

        questionId = getTemplateProvideService().extractQuestionIdByValue(
                getTemplateProvideService().getTemplate(forTemplateType()).getObjectFeatureQuestions(),
                OBJECT_FEATURE_COVID_19_USED_AMOUNT);

        answer = new AnswerDTO();
        answer.setQuestionId(questionId);
        answer.getValues().put("amount", checklist.getCovidAmount());
        answer.getValues().put("currency", "UAH");

        answers.add(answer);

        return answers;
    }

    private List<AnswerDTO> prepareAnswersFromQuestionGroup(GroupQuestionsDTO group, ChecklistEntity checklist) {
        List<AnswerEntity> checklistAnswers = checklist.getAnswers()
                .stream()
                .filter(a -> group.getGroupName().equals(a.getCategoryName()))
                .collect(toList());

        if (isEmpty(checklistAnswers)) return Collections.emptyList();

        return group.getQuestions().stream()
                .filter(QuestionExecutionDTO::isRoot)
                .flatMap(q -> prepareRootQuestionAnswerWithChildAnswers(q, checklistAnswers).stream())
                .collect(toList());
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
                        subQuestionsByAnswerType.forEach(subId -> prepareAnswerFromSubQuestion(subId, checklistAnswer, answers));
                });

        return answers;
    }

    private void prepareAnswerFromSubQuestion(Integer questionId, AnswerEntity checklistAnswer, List<AnswerDTO> answers) {
        QuestionExecutionDTO execution = templateService.extractQuestionById(questionId, forTemplateType());
        Double answerValue = checklistAnswer.getAmount();

        if (isNull(answerValue)) return;

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setQuestionId(execution.getId());
        answerDTO.getValues().put("amount", answerValue);
        answerDTO.getValues().put("currency", "UAH");
        answers.add(answerDTO);

        execution.getSubQuestions()
                .forEach(sub -> sub.getQuestionIds()
                        .forEach(subId -> prepareAnswerFromSubQuestion(subId, checklistAnswer, answers)));
    }
}

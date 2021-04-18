package com.datapath.checklistapp.dto;

import lombok.Data;

import java.util.*;

import static java.util.Objects.isNull;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private QuestionDTO question;
    private boolean required;
    private boolean root;
    private Long linkTypeId;
    private Long nodeTypeId;
    private Integer orderNumber;
    private Long roleId;
    private Long parentQuestionId;
    private String parentConditionFieldName;
    private Long parentConditionAnswerId;

    private List<SubQuestions> subQuestions = new ArrayList<>();
    private List<AutoCompleteConfigDTO> autoCompleteConfigs = new ArrayList<>();

    @Data
    private static class SubQuestions {
        private String conditionFieldName;
        private Map<Long, Set<Long>> conditionAnswerQuestionIds = new HashMap<>();
        private Set<Long> questionIds = new HashSet<>();


        private SubQuestions(String conditionFieldName) {
            this.conditionFieldName = conditionFieldName;
        }
    }

    public void addSubQuestions(Set<Long> subQuestionIds) {
        subQuestionIds.forEach(s -> this.addSubQuestion(s, null, null));
    }

    public void addSubQuestion(Long subQuestionId, String conditionFieldName, Long conditionAnswerId) {
        SubQuestions sub;

        if (isNull(conditionFieldName)) {
            sub = subQuestions.stream()
                    .findFirst()
                    .orElseGet(() -> {
                        SubQuestions newSub = new SubQuestions(null);
                        subQuestions.add(newSub);
                        return newSub;
                    });
        } else {
            sub = subQuestions.stream()
                    .filter(s -> conditionFieldName.equals(s.conditionFieldName))
                    .findFirst()
                    .orElseGet(() -> {
                        SubQuestions newSub = new SubQuestions(conditionFieldName);
                        subQuestions.add(newSub);
                        return newSub;
                    });
        }

        if (isNull(conditionAnswerId)) {
            sub.questionIds.add(subQuestionId);
        } else {
            sub.conditionAnswerQuestionIds.computeIfPresent(conditionAnswerId, (k, v) -> {
                v.add(subQuestionId);
                return v;
            });
            sub.conditionAnswerQuestionIds.computeIfAbsent(conditionAnswerId, k -> {
                Set<Long> values = new HashSet<>();
                values.add(subQuestionId);
                return values;
            });
        }
    }
}

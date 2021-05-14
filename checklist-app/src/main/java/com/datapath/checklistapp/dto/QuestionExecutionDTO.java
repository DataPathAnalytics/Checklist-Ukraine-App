package com.datapath.checklistapp.dto;

import lombok.Data;

import java.util.*;

import static java.util.Objects.isNull;

@Data
public class QuestionExecutionDTO {

    private Integer id;
    private QuestionDTO question;
    private boolean required;
    private boolean root;
    private String linkType;
    private String nodeType;
    private Integer orderNumber;
    private Long miningRoleId;
    private Integer parentQuestionId;
    private String parentConditionFieldName;
    private Integer parentConditionAnswerId;

    private List<ConditionCharacteristicDTO> conditionCharacteristics;
    private List<SubQuestions> subQuestions = new ArrayList<>();
    private List<AutoCompleteConfigDTO> autoCompleteConfigs;

    @Data
    private static class SubQuestions {
        private String conditionFieldName;
        private Map<Integer, Set<Integer>> conditionAnswerQuestionIds = new HashMap<>();
        private Set<Integer> questionIds = new HashSet<>();


        private SubQuestions(String conditionFieldName) {
            this.conditionFieldName = conditionFieldName;
        }
    }

    public void addSubQuestions(Set<Integer> subQuestionIds) {
        subQuestionIds.forEach(s -> this.addSubQuestion(s, null, null));
    }

    public void addSubQuestion(Integer subQuestionId, String conditionFieldName, Integer conditionAnswerId) {
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
                Set<Integer> values = new HashSet<>();
                values.add(subQuestionId);
                return values;
            });
        }
    }
}

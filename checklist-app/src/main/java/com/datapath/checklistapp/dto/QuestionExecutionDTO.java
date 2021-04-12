package com.datapath.checklistapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
public class QuestionExecutionDTO {

    private Long id;
    private QuestionDTO question;
    private boolean required;
    private boolean root;
    private Long linkTypeId;
    private Long nodeTypeId;

    private List<SubQuestions> subQuestions = new ArrayList<>();
    private List<AutoCompleteConfigDTO> autoCompleteConfigs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    private static class SubQuestions {
        private Long conditionAnswerId;
        private Set<Long> questionIds = new HashSet<>();

        private SubQuestions(Long conditionAnswerId) {
            this.conditionAnswerId = conditionAnswerId;
        }
    }

    public void addSubQuestions(Set<Long> subQuestionIds) {
        subQuestionIds.forEach(s -> this.addSubQuestion(s, null));
    }

    public void addSubQuestion(Long subQuestionId, Long conditionAnswerId) {
        SubQuestions sub;

        if (isNull(conditionAnswerId)) {
            sub = subQuestions.stream()
                    .findFirst()
                    .orElseGet(SubQuestions::new);
        } else {
            sub = subQuestions.stream()
                    .filter(s -> conditionAnswerId.equals(s.conditionAnswerId))
                    .findFirst()
                    .orElseGet(() -> new SubQuestions(conditionAnswerId));
        }

        sub.questionIds.add(subQuestionId);
    }
}

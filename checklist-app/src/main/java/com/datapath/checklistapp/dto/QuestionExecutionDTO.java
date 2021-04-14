package com.datapath.checklistapp.dto;

import lombok.Data;

import java.util.*;

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
    private static class SubQuestions {
        private String conditionFieldName;
        private Map<Long, List<Long>> conditionAnswerQuestionIds = new HashMap<>();

        private SubQuestions(String conditionFieldName) {
            this.conditionFieldName = conditionFieldName;
        }
    }

    public void addSubQuestions(Set<Long> subQuestionIds) {
//        subQuestionIds.forEach(s -> this.addSubQuestion(s, null));
    }

    public void addSubQuestion(Long subQuestionId, Long conditionAnswerId, String conditionFieldName) {
//        Optional<SubQuestions> sub;
//
//        if (isNull(conditionAnswerId)) {
//            sub = subQuestions.stream()
//                    .findFirst();
//        } else {
//            sub = subQuestions.stream()
//                    .filter(s -> conditionAnswerId.equals(s.conditionAnswerId))
//                    .findFirst();
//        }
//
//        if (sub.isPresent()) {
//            sub.get().questionIds.add(subQuestionId);
//        } else {
//            SubQuestions newSub = new SubQuestions(conditionAnswerId);
//            newSub.questionIds.add(subQuestionId);
//            subQuestions.add(newSub);
//        }
    }
}

package com.datapath.checklistapp.dto.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTemplateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long folderId;
    @NotNull
    private Long templateConfigId;

    private List<TemplateQuestion> ungroupedQuestions = new ArrayList<>();
    private List<QuestionGroup> questionGroups = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionGroup {
        @NotNull
        private String name;
        @NotNull
        private Integer orderNumber;
        private List<TemplateQuestion> questions = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateQuestion {
        @NotNull
        private Long questionId;

        private Long parentQuestionId;
        private Integer parentConditionAnswerId;

        private Long interpretationId;
        private Integer interpretationConditionAnswerId;

        private boolean required;

        @NotNull
        private Integer orderNumber;
    }
}

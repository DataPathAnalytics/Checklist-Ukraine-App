package com.datapath.checklistapp.dto.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
    @Valid
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
    @Valid
    public static class TemplateQuestion {
        @NotNull
        private Long questionId;

        private Long parentQuestionId;
        private Integer parentConditionAnswerId;

        private List<ConditionCharacteristic> conditionCharacteristics = new ArrayList<>();

        private boolean required;
        private String linkType;

        @NotNull
        private Integer orderNumber;
    }

    @Data
    @Valid
    public static class ConditionCharacteristic {

        private boolean evaluation;
        private Long riskEventTypeId;

        @NotNull
        private Integer conditionAnswerId;
    }
}

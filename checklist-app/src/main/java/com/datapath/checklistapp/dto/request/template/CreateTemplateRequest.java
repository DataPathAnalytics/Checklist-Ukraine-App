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

    @Valid
    private List<TemplateQuestion> ungroupedQuestions = new ArrayList<>();
    @Valid
    private List<QuestionGroup> questionGroups = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionGroup {
        @NotNull
        private String name;
        @NotNull
        private Integer orderNumber;
        @Valid
        private List<TemplateQuestion> questions = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateQuestion {
        @NotNull
        private Long questionId;
        @NotNull
        private Integer orderNumber;

        private boolean required;
        private Long linkTypeId;
        private Long nodeTypeId;

        @Valid
        private List<ConditionCharacteristic> conditionCharacteristics = new ArrayList<>();
        @Valid
        private List<SubQuestion> subQuestions = new ArrayList<>();
    }

    @Data
    public static class ConditionCharacteristic {
        @NotNull
        private Long riskEventTypeId;
        @NotNull
        private Long conditionAnswerId;
    }

    @Data
    public static class SubQuestion {
        private Long conditionAnswerId;

        @NotBlank
        private String conditionFieldName;
        @NotNull
        private TemplateQuestion question;
    }
}

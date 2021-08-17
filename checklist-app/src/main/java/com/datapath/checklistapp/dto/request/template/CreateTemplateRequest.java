package com.datapath.checklistapp.dto.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.UNKNOWN_NODE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTemplateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer folderId;
    @NotNull
    private Integer templateConfigId;

    @Valid
    private List<TemplateQuestion> questions = new ArrayList<>();
    @Valid
    private List<QuestionGroup> groups = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionGroup {
        @NotNull
        private String name;

        private Integer orderNumber = 1;
        @Valid
        private List<TemplateQuestion> questions = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateQuestion {
        @NotNull
        private Integer questionId;

        private Integer orderNumber = 1;
        private boolean required;
        private String linkType;
        private String nodeType = UNKNOWN_NODE;

        @NotBlank
        private String hash;
        private String parentHash;
        private String conditionFieldName;
        private Integer conditionAnswerId;

        @Valid
        private List<ConditionCharacteristic> conditionCharacteristics = new ArrayList<>();
    }

    @Data
    public static class ConditionCharacteristic {
        @NotNull
        private Long riskEventTypeId;
        @NotNull
        private Integer conditionAnswerId;
    }

    public static QuestionGroup toGroup(String name, List<TemplateQuestion> questions) {
        QuestionGroup group = new QuestionGroup();
        group.setName(name);
        group.setOrderNumber(-1);
        group.setQuestions(questions);
        return group;
    }
}

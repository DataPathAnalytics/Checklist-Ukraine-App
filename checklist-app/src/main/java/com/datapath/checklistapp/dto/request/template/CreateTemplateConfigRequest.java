package com.datapath.checklistapp.dto.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.ACTIVITY_TEMPLATE_TYPE;
import static java.util.Objects.nonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateConfigRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer templateConfigTypeId;
    @NotNull
    private Long folderId;

    @NotNull
    private BaseQuestion objectQuestion;
    private BaseQuestion authorityQuestion;

    private List<TemplateQuestion> objectFeatureQuestions = new ArrayList<>();
    private List<TemplateQuestion> typeQuestions = new ArrayList<>();
    private List<TemplateQuestion> authorityFeatureQuestions = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class BaseQuestion {
        @NotNull
        private Long questionId;
        private List<AutoCompleteConfig> autoCompleteConfigs = new ArrayList<>();

        public TemplateQuestion asTemplateQuestion() {
            TemplateQuestion question = new TemplateQuestion();
            question.setQuestionId(this.questionId);
            question.setAutoCompleteConfigs(this.getAutoCompleteConfigs());
            return question;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class TemplateQuestion {
        @NotNull
        private Long questionId;
        private Long parentQuestionId;
        @NotNull
        private Integer orderNumber;
        private boolean required;

        @NotNull
        private Long nodeTypeId;
        @NotNull
        private Long linkTypeId;

        private List<AutoCompleteConfig> autoCompleteConfigs = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Valid
    public static class AutoCompleteConfig {
        @NotNull
        private Long fieldId;
        private boolean autoComplete;
        private boolean searchable;
        private String datasource;
        private String filterFieldName;
        @NotBlank
        private String fieldName;

        @AssertTrue
        public Boolean isValid() {
            if (searchable) {
                return nonNull(datasource);
            }
            return true;
        }
    }

    @AssertTrue
    public Boolean isValid() {
        if (templateConfigTypeId.equals(ACTIVITY_TEMPLATE_TYPE)) {
            return nonNull(authorityQuestion);
        }
        return true;
    }
}

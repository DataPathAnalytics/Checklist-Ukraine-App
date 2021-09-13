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
import static com.datapath.checklistapp.util.Constants.UNKNOWN_NODE;
import static java.util.Objects.nonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveTemplateConfigRequest {

    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private Integer templateConfigTypeId;
    @NotNull
    private Integer folderId;

    @NotNull
    private BaseQuestion objectQuestion;
    private BaseQuestion authorityQuestion;

    @Valid
    private List<TemplateConfigQuestion> objectFeatureQuestions = new ArrayList<>();
    @Valid
    private List<TemplateConfigQuestion> typeQuestions = new ArrayList<>();
    @Valid
    private List<TemplateConfigQuestion> authorityFeatureQuestions = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaseQuestion {
        @NotNull
        private Integer questionId;

        private String nodeType = UNKNOWN_NODE;
        private Long miningRoleId;

        @Valid
        private List<AutoCompleteConfig> autoCompleteConfigs = new ArrayList<>();

        public TemplateConfigQuestion asTemplateQuestion() {
            TemplateConfigQuestion question = new TemplateConfigQuestion();
            question.setQuestionId(this.questionId);
            question.setNodeType(this.nodeType);
            question.setMiningRoleId(this.miningRoleId);
            question.setAutoCompleteConfigs(this.autoCompleteConfigs);
            return question;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateConfigQuestion {
        @NotNull
        private Integer questionId;

        private Integer orderNumber = 1;
        private String nodeType = UNKNOWN_NODE;
        private String linkType;
        private Long miningRoleId;
        private boolean required;

        @NotBlank
        private String hash;
        private String parentHash;

        @Valid
        private List<AutoCompleteConfig> autoCompleteConfigs = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutoCompleteConfig {
        @NotNull
        private Integer fieldId;
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

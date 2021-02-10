package com.datapath.checklistapp.dto.request.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateConfigRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer templateTypeId;
    @NotNull
    private Long folderId;

    @NotNull
    private Long objectQuestionId;

    private List<TemplateQuestion> featureQuestions = new ArrayList<>();
    private List<TemplateQuestion> typeQuestions = new ArrayList<>();
    private List<TemplateQuestion> authorityQuestions = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateQuestion {
        @NotNull
        private Long questionId;
        private Long parentQuestionId;
        private Integer orderNumber;
    }
}

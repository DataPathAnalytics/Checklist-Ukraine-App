package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.service.CatalogWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("catalogs")
@Api(value = "Operation with static application content")
public class CatalogController {

    private final CatalogWebService service;

    @ApiOperation(value = "list of available question sources", response = QuestionSourceDTO.class)
    @GetMapping("question-sources")
    public List<QuestionSourceDTO> questionSourceList() {
        return service.getQuestionSources();
    }

    @ApiOperation(value = "list of available knowledge categories", response = IdValueDTO.class)
    @GetMapping("knowledge-categories")
    public List<IdValueDTO> knowledgeCategoryList() {
        return service.getKnowledgeCategories();
    }

    @ApiOperation(value = "list of available interpretations", response = IdValueDTO.class)
    @GetMapping("interpretations")
    public List<IdValueDTO> interpretationList() {
        return service.getInterpretations();
    }

    @ApiOperation(value = "list of available answer structures", response = AnswerStructureDTO.class)
    @GetMapping
    public List<AnswerStructureDTO> list() {
        return service.getAnswerStructures();
    }
}

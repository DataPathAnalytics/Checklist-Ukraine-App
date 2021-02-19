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
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<QuestionSourceDTO> questionSources() {
        return service.getQuestionSources();
    }

    @ApiOperation(value = "list of available knowledge categories", response = IdValueDTO.class)
    @GetMapping("knowledge-categories")
    public List<IdValueDTO> knowledgeCategories() {
        return service.getKnowledgeCategories();
    }

    @ApiOperation(value = "search knowledge categories by name", response = IdValueDTO.class)
    @GetMapping("knowledge-categories/search")
    public List<IdValueDTO> searchKnowledgeCategories(@RequestParam String name) {
        return service.searchKnowledgeCategories(name);
    }

    @ApiOperation(value = "list of available interpretations", response = IdValueDTO.class)
    @GetMapping("interpretations")
    public List<IdValueDTO> interpretationList() {
        return service.getInterpretations();
    }

    @ApiOperation(value = "list of available answer structures", response = AnswerStructureDTO.class)
    @GetMapping("answer-structures")
    public List<AnswerStructureDTO> answerStructures() {
        return service.getAnswerStructures();
    }

    @ApiOperation(value = "search answer structures by name", response = AnswerStructureDTO.class)
    @GetMapping("answer-structures/search")
    public List<AnswerStructureDTO> searchAnswerStructures(@RequestParam String name) {
        return service.searchAnswerStructures(name);
    }
}

package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.search.SearchResponse;
import com.datapath.checklistapp.service.CatalogWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "search knowledge categories by name", response = SearchResponse.class)
    @PostMapping("knowledge-categories/search")
    public SearchResponse<IdValueDTO> searchKnowledgeCategories(@RequestBody SearchRequest request) {
        return service.searchKnowledgeCategories(request);
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
    @PostMapping("answer-structures/search")
    public SearchResponse<AnswerStructureDTO> searchAnswerStructures(@RequestBody SearchRequest request) {
        return service.searchAnswerStructures(request);
    }
}

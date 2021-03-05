package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
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

    @ApiOperation(value = "list of available answer structures", response = AnswerStructureDTO.class)
    @GetMapping("answer-structures")
    public List<AnswerStructureDTO> answerStructures() {
        return service.getAnswerStructures();
    }

    @ApiOperation(value = "search answer structures by name", response = AnswerStructureDTO.class)
    @PostMapping("answer-structures/search")
    public PageableResponse<AnswerStructureDTO> searchAnswerStructures(@RequestBody SearchRequest request) {
        return service.searchAnswerStructures(request);
    }
}

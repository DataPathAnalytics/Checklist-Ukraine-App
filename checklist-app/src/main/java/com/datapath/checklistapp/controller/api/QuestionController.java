package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionTypeDTO;
import com.datapath.checklistapp.dto.request.question.CreateQuestionRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.search.SearchResponse;
import com.datapath.checklistapp.service.QuestionWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("questions")
@Api(value = "Operation with questions")
public class QuestionController {

    private final QuestionWebService service;

    @ApiOperation(value = "create question")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateQuestionRequest request) {
        service.create(request);
    }

    @ApiOperation(value = "list of questions", response = QuestionDTO.class)
    @GetMapping
    public List<QuestionDTO> list() {
        return service.list();
    }

    @ApiOperation(value = "list of questions by template config type", response = QuestionTypeDTO.class)
    @GetMapping("template-config-type/{id}")
    public List<QuestionTypeDTO> listByTemplateConfigType(@PathVariable Integer id) {
        return service.listByTemplateConfigType(id);
    }

    @ApiOperation(value = "list of questions by question type", response = QuestionTypeDTO.class)
    @GetMapping("question-type/{id}")
    public List<QuestionTypeDTO> listByQuestionType(@PathVariable Integer id) {
        return service.listByQuestionType(id);
    }

    @ApiOperation(value = "get question by id", response = QuestionDTO.class)
    @GetMapping("{id}")
    public QuestionDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @ApiOperation(value = "search question by name", response = QuestionDTO.class)
    @PostMapping("search")
    public SearchResponse<QuestionDTO> search(@RequestBody SearchRequest request) {
        return service.search(request);
    }
}

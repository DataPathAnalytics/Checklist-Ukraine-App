package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.request.question.CreateQuestionRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.QuestionWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("questions")
public class QuestionController {

    private final QuestionWebService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateQuestionRequest request) {
        service.create(request);
    }

    @GetMapping("{id}")
    public QuestionDTO get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping("search")
    public PageableResponse<QuestionDTO> search(@RequestBody SearchRequest request) {
        return service.search(request);
    }

    @PostMapping("search/identifier")
    public PageableResponse<QuestionDTO> searchWithIdentifier(@RequestBody SearchRequest request) {
        return service.searchWithIdentifier(request);
    }
}

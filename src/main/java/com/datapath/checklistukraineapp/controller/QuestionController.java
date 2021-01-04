package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.request.CreateQuestionRequest;
import com.datapath.checklistukraineapp.service.QuestionWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("question")
public class QuestionController {

    private final QuestionWebService service;

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateQuestionRequest request) {
        service.create(request);
    }

    @GetMapping
    public List<QuestionDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public QuestionDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}

package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionTypeDTO;
import com.datapath.checklistapp.dto.request.question.CreateQuestionRequest;
import com.datapath.checklistapp.service.QuestionWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("questions")
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

    @GetMapping("template-type/{id}")
    public List<QuestionTypeDTO> templateTypeList(@PathVariable Integer id) {
        return service.listByTemplateType(id);
    }

    @GetMapping("question-type/{id}")
    public List<QuestionTypeDTO> questionTypeList(@PathVariable Integer id) {
        return service.listByQuestionType(id);
    }

    @GetMapping("{id}")
    public QuestionDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}
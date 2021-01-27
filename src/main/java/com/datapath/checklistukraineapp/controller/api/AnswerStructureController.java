package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.AnswerStructureDTO;
import com.datapath.checklistukraineapp.service.AnswerStructureWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("answer-structures")
@AllArgsConstructor
public class AnswerStructureController {

    private final AnswerStructureWebService service;

    @GetMapping
    public List<AnswerStructureDTO> list() {
        return service.list();
    }
}

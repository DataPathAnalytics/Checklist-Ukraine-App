package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.service.CatalogWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("catalogs")
public class CatalogController {

    private final CatalogWebService service;

    @GetMapping("question-source")
    public List<QuestionSourceDTO> questionSourceList() {
        return service.getQuestionSources();
    }
}

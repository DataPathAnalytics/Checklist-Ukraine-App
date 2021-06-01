package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.CatalogWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("catalog")
public class CatalogController {

    private final CatalogWebService service;

    @PostMapping("answer-structures")
    public PageableResponse<AnswerStructureDTO> searchAnswerStructures(@RequestBody SearchRequest request) {
        return service.searchAnswerStructures(request);
    }

    @GetMapping("answer-structures")
    public List<AnswerStructureDTO> allAnswerStructures() {
        return service.allAnswerStructures();
    }
}

package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.CatalogWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("catalog")
public class CatalogController {

    private final CatalogWebService service;

    @PostMapping("answer-structures")
    public PageableResponse<AnswerStructureDTO> answerStructures(@RequestBody SearchRequest request) {
        return service.searchAnswerStructures(request);
    }
}

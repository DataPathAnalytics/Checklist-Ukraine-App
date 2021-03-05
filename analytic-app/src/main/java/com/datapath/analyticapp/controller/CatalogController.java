package com.datapath.analyticapp.controller;

import com.datapath.analyticapp.dto.InterpretationDTO;
import com.datapath.analyticapp.dto.KnowledgeCategoryDTO;
import com.datapath.analyticapp.dto.request.SearchRequest;
import com.datapath.analyticapp.dto.response.PageableResponse;
import com.datapath.analyticapp.service.web.CatalogWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("catalogs")
public class CatalogController {

    private final CatalogWebService service;

    @PostMapping("knowledge-categories")
    public PageableResponse<KnowledgeCategoryDTO> searchKnowledgeCategories(@RequestBody SearchRequest request) {
        return service.searchKnowledgeCategories(request);
    }

    @PostMapping("interpretations")
    public PageableResponse<InterpretationDTO> searchInterpretations(@RequestBody SearchRequest request) {
        return service.searchInterpretations(request);
    }
}

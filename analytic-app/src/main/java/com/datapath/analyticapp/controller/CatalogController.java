package com.datapath.analyticapp.controller;

import com.datapath.analyticapp.dto.EventTypeDTO;
import com.datapath.analyticapp.dto.KnowledgeClassDTO;
import com.datapath.analyticapp.dto.NodeTypeDTO;
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

    @PostMapping("knowledge-class")
    public PageableResponse<KnowledgeClassDTO> searchKnowledgeClasses(@RequestBody SearchRequest request) {
        return service.searchKnowledgeClasses(request);
    }

    @PostMapping("event-type")
    public PageableResponse<EventTypeDTO> searchEventTypes(@RequestBody SearchRequest request) {
        return service.searchEventTypes(request);
    }

    @PostMapping("node-type")
    public PageableResponse<NodeTypeDTO> searchNodeTypes(@RequestBody SearchRequest request) {
        return service.searchNodeTypes(request);
    }
}

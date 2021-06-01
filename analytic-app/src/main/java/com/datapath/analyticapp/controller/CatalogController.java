package com.datapath.analyticapp.controller;

import com.datapath.analyticapp.dto.EventTypeDTO;
import com.datapath.analyticapp.dto.KnowledgeClassDTO;
import com.datapath.analyticapp.dto.RoleDTO;
import com.datapath.analyticapp.dto.request.SearchRequest;
import com.datapath.analyticapp.dto.response.PageableResponse;
import com.datapath.analyticapp.service.web.CatalogWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("catalogs")
@CrossOrigin
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

    @PostMapping("role")
    public PageableResponse<RoleDTO> searchRoles(@RequestBody SearchRequest request) {
        return service.searchRoles(request);
    }
}

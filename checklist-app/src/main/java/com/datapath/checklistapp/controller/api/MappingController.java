package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.response.mapping.MappingResponse;
import com.datapath.checklistapp.service.web.MappingWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mappings")
@AllArgsConstructor
public class MappingController {

    private final MappingWebService service;

    @GetMapping
    public MappingResponse mappings() {
        return service.getMappings();
    }
}

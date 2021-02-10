package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.response.mapping.MappingPrivateResponse;
import com.datapath.checklistapp.dto.response.mapping.MappingPublicResponse;
import com.datapath.checklistapp.service.MappingWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mappings")
@AllArgsConstructor
public class MappingController {

    private final MappingWebService service;

    @GetMapping("public")
    public MappingPublicResponse getPublicMappings() {
        return service.getPublicMappings();
    }

    @GetMapping("private")
    public MappingPrivateResponse getPrivateMappings() {
        return service.getPrivateMappings();
    }
}

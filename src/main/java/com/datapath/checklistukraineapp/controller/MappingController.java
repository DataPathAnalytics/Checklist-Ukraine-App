package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.response.MappingPrivateResponse;
import com.datapath.checklistukraineapp.domain.response.MappingPublicResponse;
import com.datapath.checklistukraineapp.service.MappingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mapping")
@AllArgsConstructor
public class MappingController {

    private final MappingService service;

    @GetMapping("public")
    public MappingPublicResponse getPublicMappings() {
        return service.getPublicMappings();
    }

    @GetMapping("private")
    public MappingPrivateResponse getPrivateMappings() {
        return service.getPrivateMappings();
    }
}

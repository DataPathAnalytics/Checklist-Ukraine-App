package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.response.MappingPrivateResponse;
import com.datapath.checklistukraineapp.dto.response.MappingPublicResponse;
import com.datapath.checklistukraineapp.service.MappingWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mapping")
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

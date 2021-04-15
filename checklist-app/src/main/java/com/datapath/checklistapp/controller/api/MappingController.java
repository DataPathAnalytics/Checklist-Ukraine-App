package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.response.mapping.MappingPrivateResponse;
import com.datapath.checklistapp.service.MappingWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mappings")
@AllArgsConstructor
@Api(value = "Getting application mappings")
public class MappingController {

    private final MappingWebService service;

    @ApiOperation(value = "list of private folders", response = MappingPrivateResponse.class)
    @GetMapping("private")
    public MappingPrivateResponse getPrivateMappings() {
        return service.getPrivateMappings();
    }
}

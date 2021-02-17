package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.service.TemplateWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("templates")
@AllArgsConstructor
@Api(value = "Operation with templates")
public class TemplateController {

    private final TemplateWebService service;

    @ApiOperation(value = "create template config")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping("config")
    @PreAuthorize("hasAuthority('methodologist')")
    public void createConfig(@RequestBody @Valid CreateTemplateConfigRequest request) {
        service.create(request);
    }

    @ApiOperation(value = "list of template configs", response = TemplateFolderTreeDTO.class)
    @GetMapping("config")
    public List<TemplateFolderTreeDTO> configList(@RequestParam(required = false) Integer templateType) {
        return service.configList(templateType);
    }

    @ApiOperation(value = "get template config by id", response = TemplateDTO.class)
    @GetMapping("config/{id}")
    public TemplateDTO getConfig(@PathVariable Long id) {
        return service.getConfig(id);
    }

    @ApiOperation(value = "create template")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateTemplateRequest request) {
        service.create(request);
    }

    @ApiOperation(value = "list of templates", response = TemplateFolderTreeDTO.class)
    @GetMapping
    public List<TemplateFolderTreeDTO> list() {
        return service.list();
    }

    @ApiOperation(value = "get template by id", response = TemplateDTO.class)
    @GetMapping("{id}")
    public TemplateDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}

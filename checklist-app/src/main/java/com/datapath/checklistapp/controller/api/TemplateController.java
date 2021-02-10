package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.template.CreateTemplateConfigRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.service.TemplateWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("templates")
@AllArgsConstructor
public class TemplateController {

    private final TemplateWebService service;

    @PostMapping("config")
    @PreAuthorize("hasAuthority('methodologist')")
    public void createConfig(@RequestBody @Valid CreateTemplateConfigRequest request) {
        service.create(request);
    }

    @GetMapping("config")
    public List<TemplateFolderTreeDTO> configList(@RequestParam(required = false) Integer templateType) {
        return service.configList(templateType);
    }

    @GetMapping("config/{id}")
    public TemplateDTO getConfig(@PathVariable Long id) {
        return service.getConfig(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateTemplateRequest request) {
        service.create(request);
    }

    @GetMapping
    public List<TemplateFolderTreeDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public TemplateDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}

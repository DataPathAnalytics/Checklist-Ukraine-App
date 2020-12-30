package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.dto.TemplateDTO;
import com.datapath.checklistukraineapp.domain.request.template.CreateTemplateRequest;
import com.datapath.checklistukraineapp.service.TemplateWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("template")
@AllArgsConstructor
public class TemplateController {

    private final TemplateWebService service;

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateTemplateRequest dto) {
        service.save(dto);
    }

    @GetMapping
    public List<TemplateDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public TemplateDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}

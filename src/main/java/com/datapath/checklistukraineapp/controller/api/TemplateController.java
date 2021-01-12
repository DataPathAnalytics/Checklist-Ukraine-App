package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistukraineapp.dto.response.TemplateResponse;
import com.datapath.checklistukraineapp.service.TemplateWebService;
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

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateTemplateRequest dto) {
        service.create(dto);
    }

    @GetMapping
    public List<TemplateDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public TemplateResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}

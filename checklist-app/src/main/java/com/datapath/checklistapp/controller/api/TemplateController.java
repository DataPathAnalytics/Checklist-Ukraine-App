package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.TemplateWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateTemplateRequest request) {
        service.create(request);
    }

    @GetMapping
    public List<TemplateFolderTreeDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public TemplateDTO get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping("search")
    public PageableResponse<TemplateDTO> search(@RequestBody SearchRequest request) {
        return service.search(request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('methodologist')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}

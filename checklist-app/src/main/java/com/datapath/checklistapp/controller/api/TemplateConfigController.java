package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.SaveTemplateConfigRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.TemplateConfigWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("template-configs")
@AllArgsConstructor
public class TemplateConfigController {

    private final TemplateConfigWebService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public void save(@RequestBody @Valid SaveTemplateConfigRequest request) {
        service.create(request);
    }

    @GetMapping
    public List<TemplateFolderTreeDTO> list(@RequestParam(required = false) Integer templateType) {
        return service.list(templateType);
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

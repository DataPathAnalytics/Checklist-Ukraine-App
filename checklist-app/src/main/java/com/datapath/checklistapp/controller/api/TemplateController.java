package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.TemplateDTO;
import com.datapath.checklistapp.dto.TemplateFolderTreeDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.request.template.CreateTemplateRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.TemplateWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @ApiOperation(value = "create template")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
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
    public TemplateDTO get(@PathVariable Integer id) {
        return service.get(id);
    }

    @ApiOperation(value = "search template by name", response = TemplateDTO.class)
    @PostMapping("search")
    public PageableResponse<TemplateDTO> search(@RequestBody SearchRequest request) {
        return service.search(request);
    }

    @ApiOperation(value = "delete template by id")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('methodologist')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}

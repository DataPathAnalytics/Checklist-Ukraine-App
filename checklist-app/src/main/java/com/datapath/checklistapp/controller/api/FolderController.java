package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.request.folder.CreateFolderRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.search.SearchResponse;
import com.datapath.checklistapp.service.FolderWebService;
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
@RequestMapping("folders")
@AllArgsConstructor
@Api(value = "Operation with template/templateConfig folders")
public class FolderController {

    private final FolderWebService service;

    @ApiOperation(value = "create template folder", response = FolderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping("template")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public List<FolderDTO> create(@RequestBody @Valid CreateFolderRequest request) {
        service.createTemplateFolder(request);
        return service.getTemplateFolders();
    }

    @ApiOperation(value = "list of template folders", response = FolderDTO.class)
    @GetMapping("template")
    public List<FolderDTO> list() {
        return service.getTemplateFolders();
    }

    @ApiOperation(value = "create template config folder", response = FolderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Available only for users with role 'methodologist'")
    })
    @PostMapping("template-config")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public List<FolderDTO> createConfig(@RequestBody @Valid CreateFolderRequest request) {
        service.createTemplateConfigFolder(request);
        return service.getTemplateConfigFolders();
    }

    @ApiOperation(value = "list of template config folders", response = FolderDTO.class)
    @GetMapping("template-config")
    public List<FolderDTO> listConfig() {
        return service.getTemplateConfigFolders();
    }

    @ApiOperation(value = "search template config folders by name", response = FolderDTO.class)
    @PostMapping("template-config/search")
    public SearchResponse<FolderDTO> searchConfig(@RequestBody SearchRequest request) {
        return service.searchTemplateConfigFolders(request);
    }

    @ApiOperation(value = "search template folders by name", response = FolderDTO.class)
    @PostMapping("template/search")
    public SearchResponse<FolderDTO> search(@RequestBody SearchRequest request) {
        return service.searchTemplateFolders(request);
    }
}

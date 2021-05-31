package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.request.folder.FolderCreateRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.FolderWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("folders")
@AllArgsConstructor
public class FolderController {

    private final FolderWebService service;

    @PostMapping("templates")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public PageableResponse<FolderDTO> createTemplateFolder(@RequestBody @Valid FolderCreateRequest request) {
        service.createTemplateFolder(request);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword(request.getName());
        return service.searchTemplateFolders(searchRequest);
    }

    @PostMapping("template-configs")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('methodologist')")
    public PageableResponse<FolderDTO> createTemplateConfigFolder(@RequestBody @Valid FolderCreateRequest request) {
        service.createTemplateConfigFolder(request);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword(request.getName());
        return service.searchTemplateConfigFolders(searchRequest);
    }

    @PostMapping("template-configs/search")
    public PageableResponse<FolderDTO> searchTemplateConfigFolder(@RequestBody SearchRequest request) {
        return service.searchTemplateConfigFolders(request);
    }

    @PostMapping("templates/search")
    public PageableResponse<FolderDTO> searchTemplateFolder(@RequestBody SearchRequest request) {
        return service.searchTemplateFolders(request);
    }
}

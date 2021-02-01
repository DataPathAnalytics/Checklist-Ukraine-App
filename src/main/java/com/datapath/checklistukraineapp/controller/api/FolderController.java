package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.request.folder.CreateFolderRequest;
import com.datapath.checklistukraineapp.service.FolderWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("folders")
@AllArgsConstructor
public class FolderController {

    private final FolderWebService service;

    @PostMapping("template")
    @PreAuthorize("hasAuthority('methodologist')")
    public List<FolderDTO> create(@RequestBody @Valid CreateFolderRequest request) {
        service.createTemplateFolder(request);
        return service.getTemplateFolders();
    }

    @GetMapping("template")
    public List<FolderDTO> list() {
        return service.getTemplateFolders();
    }

    @PostMapping("template-config")
    @PreAuthorize("hasAuthority('methodologist')")
    public List<FolderDTO> createConfig(@RequestBody @Valid CreateFolderRequest request) {
        service.createTemplateConfigFolder(request);
        return service.getTemplateConfigFolders();
    }

    @GetMapping("template-config")
    public List<FolderDTO> listConfig() {
        return service.getTemplateConfigFolders();
    }
}

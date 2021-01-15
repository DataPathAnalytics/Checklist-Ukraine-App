package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.request.CreateFolderRequest;
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

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public FolderDTO create(@RequestBody @Valid CreateFolderRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<FolderDTO> list() {
        return service.list();
    }
}

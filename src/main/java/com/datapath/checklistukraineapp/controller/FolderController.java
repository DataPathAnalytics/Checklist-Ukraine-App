package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.dto.FolderDTO;
import com.datapath.checklistukraineapp.domain.request.CreateFolderRequest;
import com.datapath.checklistukraineapp.service.FolderWebService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("folder")
@AllArgsConstructor
public class FolderController {

    private final FolderWebService service;

    @PostMapping
    @PreAuthorize("hasAuthority('methodologist')")
    public void create(@RequestBody @Valid CreateFolderRequest request) {
        service.create(request);
    }

    @GetMapping
    public List<FolderDTO> list() {
        return service.list();
    }
}

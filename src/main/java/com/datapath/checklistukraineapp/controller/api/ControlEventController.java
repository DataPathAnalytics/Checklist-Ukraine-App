package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.request.event.AddTemplateRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.service.ControlEventWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("control-events")
public class ControlEventController {

    private final ControlEventWebService service;

    @GetMapping
    public List<ControlEventDTO> list() {
        return service.list();
    }

    @PostMapping
    public ControlEventDTO create(@RequestBody @Valid CreateControlEventRequest request) {
        return service.create(request);
    }

    @GetMapping("{id}")
    public ControlEventDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("{id}")
    public ControlEventDTO complete(@PathVariable Long id) {
        return service.complete(id);
    }

    @PostMapping("template/add")
    public ControlEventDTO addTemplate(@RequestBody @Valid AddTemplateRequest request) {
        return service.addTemplate(request);
    }
}

package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ChecklistDTO;
import com.datapath.checklistukraineapp.dto.ChecklistPageDTO;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.request.event.AddTemplateRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateChecklistRequest;
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

    @PostMapping("templates/add")
    public ControlEventDTO addTemplate(@RequestBody @Valid AddTemplateRequest request) {
        return service.addTemplate(request);
    }

    @GetMapping("checklists")
    public ChecklistPageDTO getChecklists(@RequestParam Long eventId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        return service.getChecklists(eventId, page, size);
    }

    @PostMapping("checklists")
    public ControlEventDTO addChecklist(@RequestBody @Valid CreateChecklistRequest request) {
        return service.createChecklist(request);
    }

    @GetMapping("checklists/{id}")
    public ChecklistDTO getChecklist(@PathVariable Long id) {
        return service.getChecklist(id);
    }
}

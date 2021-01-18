package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.request.event.ChecklistStatusRequest;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.dto.request.event.EventTemplateOperationRequest;
import com.datapath.checklistukraineapp.dto.request.event.SaveChecklistRequest;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistPageResponse;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistResponse;
import com.datapath.checklistukraineapp.service.ControlEventWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT_STR;
import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE_STR;

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

    @PostMapping("templates")
    public ControlEventDTO addTemplate(@RequestBody @Valid EventTemplateOperationRequest request) {
        return service.addTemplate(request);
    }

    @DeleteMapping("templates")
    public ControlEventDTO deleteTemplate(@RequestBody @Valid EventTemplateOperationRequest request) {
        return service.deleteTemplate(request);
    }

    @GetMapping("checklists")
    public ChecklistPageResponse getChecklists(@RequestParam Long eventId,
                                               @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_PAGE_STR) int page,
                                               @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_COUNT_STR) int size) {
        return service.getChecklists(eventId, page, size);
    }

    @PostMapping("checklists")
    public ChecklistResponse saveChecklist(@RequestBody @Valid SaveChecklistRequest request) {
        return service.saveChecklist(request);
    }

    @PutMapping("checklists")
    public ChecklistResponse changeStatus(@RequestBody @Valid ChecklistStatusRequest request) {
        return service.changeStatus(request);
    }

    @GetMapping("checklists/{id}")
    public ChecklistResponse getChecklist(@PathVariable Long id) {
        return service.getChecklist(id);
    }
}

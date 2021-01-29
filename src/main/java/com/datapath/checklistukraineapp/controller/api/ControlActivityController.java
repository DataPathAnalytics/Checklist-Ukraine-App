package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlActivityDTO;
import com.datapath.checklistukraineapp.dto.request.activity.ChecklistStatusRequest;
import com.datapath.checklistukraineapp.dto.request.activity.CreateControlActivityRequest;
import com.datapath.checklistukraineapp.dto.request.activity.EventTemplateOperationRequest;
import com.datapath.checklistukraineapp.dto.request.activity.SaveChecklistRequest;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistPageResponse;
import com.datapath.checklistukraineapp.dto.response.checklist.ChecklistResponse;
import com.datapath.checklistukraineapp.service.ControlActivityWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT_STR;
import static com.datapath.checklistukraineapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE_STR;

@RestController
@AllArgsConstructor
@RequestMapping("control-events")
public class ControlActivityController {

    private final ControlActivityWebService service;

    @GetMapping
    public List<ControlActivityDTO> list() {
        return service.list();
    }

    @PostMapping
    public void create(@RequestBody @Valid CreateControlActivityRequest request) {
        service.create(request);
    }

    @GetMapping("{id}")
    public ControlActivityDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("{id}")
    public ControlActivityDTO complete(@PathVariable Long id) {
        return service.complete(id);
    }

    @PostMapping("template")
    public ControlActivityDTO addTemplate(@RequestBody @Valid EventTemplateOperationRequest request) {
        return service.addTemplate(request);
    }

    @DeleteMapping("template")
    public ControlActivityDTO deleteTemplate(@RequestBody @Valid EventTemplateOperationRequest request) {
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

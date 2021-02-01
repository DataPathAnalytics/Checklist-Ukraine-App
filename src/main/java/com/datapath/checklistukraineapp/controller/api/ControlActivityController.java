package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlActivityDTO;
import com.datapath.checklistukraineapp.dto.ResponseSessionDTO;
import com.datapath.checklistukraineapp.dto.SessionPageDTO;
import com.datapath.checklistukraineapp.dto.request.activity.CreateControlActivityRequest;
import com.datapath.checklistukraineapp.dto.request.activity.ResponseSessionStatusRequest;
import com.datapath.checklistukraineapp.dto.request.activity.SaveResponseSessionRequest;
import com.datapath.checklistukraineapp.dto.request.activity.TemplateOperationRequest;
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
    public ControlActivityDTO addTemplate(@RequestBody @Valid TemplateOperationRequest request) {
        return service.addTemplate(request);
    }

    @DeleteMapping("template")
    public ControlActivityDTO deleteTemplate(@RequestBody @Valid TemplateOperationRequest request) {
        return service.deleteTemplate(request);
    }

    @GetMapping("response-session")
    public SessionPageDTO getSessions(@RequestParam Long eventId,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_PAGE_STR) int page,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_COUNT_STR) int size) {
        return service.getSessions(eventId, page, size);
    }

    @PostMapping("response-session")
    public void saveSession(@RequestBody @Valid SaveResponseSessionRequest request) {
        service.saveSession(request);
    }

    @PutMapping("response-session")
    public void changeStatus(@RequestBody @Valid ResponseSessionStatusRequest request) {
        service.changeStatus(request);
    }

    @GetMapping("response-session/{id}")
    public ResponseSessionDTO getSession(@PathVariable Long id) {
        return service.getSession(id);
    }
}

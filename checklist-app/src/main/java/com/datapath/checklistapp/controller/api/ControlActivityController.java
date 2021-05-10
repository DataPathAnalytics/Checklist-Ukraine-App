package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.request.activity.*;
import com.datapath.checklistapp.service.ControlActivityWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_COUNT_STR;
import static com.datapath.checklistapp.util.Constants.DEFAULT_EVENT_CHECKLIST_PAGE_STR;

@RestController
@AllArgsConstructor
@RequestMapping("control-activities")
public class ControlActivityController {

    private final ControlActivityWebService service;

    @GetMapping
    public List<ControlActivityDTO> list() {
        return service.list();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ControlActivityDTO create(@RequestBody @Valid CreateControlActivityRequest request) {
        return service.create(request);
    }

    @PutMapping
    public ControlActivityDTO update(@RequestBody @Valid UpdateControlActivityRequest request) {
        return service.update(request);
    }

    @GetMapping("{id}")
    public ControlActivityDTO get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PutMapping("complete/{id}")
    public ControlActivityDTO complete(@PathVariable Integer id) {
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
    public SessionPageDTO getSessions(@RequestParam Integer activityId,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_PAGE_STR) int page,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_COUNT_STR) int size) {
        return service.getSessions(activityId, page, size);
    }

    @PostMapping("response-session")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseSessionDTO saveSession(@RequestBody @Valid SaveResponseSessionRequest request) {
        return service.saveSession(request);
    }

    @PutMapping("response-session")
    public ResponseSessionDTO changeStatus(@RequestBody @Valid ResponseSessionStatusRequest request) {
        return service.changeStatus(request);
    }

    @GetMapping("response-session/{id}")
    public ResponseSessionDTO getSession(@PathVariable Integer id) {
        return service.getSession(id);
    }
}

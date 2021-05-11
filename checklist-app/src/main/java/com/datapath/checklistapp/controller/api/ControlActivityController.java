package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.request.activity.*;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.request.page.SessionPageableRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.ControlActivityWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("control-activities")
public class ControlActivityController {

    private final ControlActivityWebService service;

    @PostMapping("list")
    public PageableResponse<ControlActivityDTO> list(@RequestBody PageableRequest request) {
        return service.list(request);
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

    @PostMapping("response-session/list")
    public PageableResponse<ResponseSessionDTO> getSessions(@RequestBody @Valid SessionPageableRequest request) {
        return service.getSessions(request);
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

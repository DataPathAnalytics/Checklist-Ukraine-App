package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.request.activity.ControlActivityCreateRequest;
import com.datapath.checklistapp.dto.request.activity.ControlActivityUpdateRequest;
import com.datapath.checklistapp.dto.request.activity.TemplateOperationRequest;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.ControlActivityWebService;
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
    public ControlActivityDTO create(@RequestBody @Valid ControlActivityCreateRequest request) {
        return service.create(request);
    }

    @PutMapping
    public ControlActivityDTO update(@RequestBody @Valid ControlActivityUpdateRequest request) {
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
}

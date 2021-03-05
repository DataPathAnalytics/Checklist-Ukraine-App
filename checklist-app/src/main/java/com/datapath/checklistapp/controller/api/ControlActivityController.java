package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.ControlActivityDTO;
import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.SessionPageDTO;
import com.datapath.checklistapp.dto.request.activity.*;
import com.datapath.checklistapp.service.ControlActivityWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "Operation with control activity")
public class ControlActivityController {

    private final ControlActivityWebService service;

    @ApiOperation(value = "list of control activity", response = ControlActivityDTO.class)
    @GetMapping
    public List<ControlActivityDTO> list() {
        return service.list();
    }

    @ApiOperation(value = "create control activity")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreateControlActivityRequest request) {
        service.create(request);
    }

    @ApiOperation(value = "update control activity")
    @PutMapping
    public void update(@RequestBody @Valid UpdateControlActivityRequest request) {
        service.update(request);
    }

    @ApiOperation(value = "get control activity by id", response = ControlActivityDTO.class)
    @GetMapping("{id}")
    public ControlActivityDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @ApiOperation(value = "complete control activity", response = ControlActivityDTO.class)
    @PutMapping("complete/{id}")
    public ControlActivityDTO complete(@PathVariable Long id) {
        return service.complete(id);
    }

    @ApiOperation(value = "add template to control activity", response = ControlActivityDTO.class)
    @PostMapping("template")
    public ControlActivityDTO addTemplate(@RequestBody @Valid TemplateOperationRequest request) {
        return service.addTemplate(request);
    }

    @ApiOperation(value = "delete template from control activity", response = ControlActivityDTO.class)
    @DeleteMapping("template")
    public ControlActivityDTO deleteTemplate(@RequestBody @Valid TemplateOperationRequest request) {
        return service.deleteTemplate(request);
    }

    @ApiOperation(value = "list of response session by control activity id", response = SessionPageDTO.class)
    @GetMapping("response-session")
    public SessionPageDTO getSessions(@RequestParam Long activityId,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_PAGE_STR) int page,
                                      @RequestParam(defaultValue = DEFAULT_EVENT_CHECKLIST_COUNT_STR) int size) {
        return service.getSessions(activityId, page, size);
    }

    @ApiOperation(value = "create/update response session")
    @PostMapping("response-session")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveSession(@RequestBody @Valid SaveResponseSessionRequest request) {
        service.saveSession(request);
    }

    @ApiOperation(value = "update response session status", response = ResponseSessionDTO.class)
    @PutMapping("response-session")
    public ResponseSessionDTO changeStatus(@RequestBody @Valid ResponseSessionStatusRequest request) {
        return service.changeStatus(request);
    }

    @ApiOperation(value = "get response session by id", response = ResponseSessionDTO.class)
    @GetMapping("response-session/{id}")
    public ResponseSessionDTO getSession(@PathVariable Long id) {
        return service.getSession(id);
    }
}

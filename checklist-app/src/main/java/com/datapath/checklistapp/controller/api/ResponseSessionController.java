package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.ResponseSessionDTO;
import com.datapath.checklistapp.dto.request.response_session.CreateRequest;
import com.datapath.checklistapp.dto.request.response_session.PageableRequest;
import com.datapath.checklistapp.dto.request.response_session.StatusOperationRequest;
import com.datapath.checklistapp.dto.request.response_session.UpdateRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.web.ResponseSessionWebService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("response-sessions")
public class ResponseSessionController {

    private final ResponseSessionWebService service;

    @PostMapping("list")
    public PageableResponse<ResponseSessionDTO> list(@RequestBody @Valid PageableRequest request) {
        return service.list(request);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseSessionDTO create(@RequestBody @Valid CreateRequest request) {
        return service.create(request);
    }

    @PutMapping
    public ResponseSessionDTO update(@RequestBody @Valid UpdateRequest request) {
        return service.update(request);
    }

    @PostMapping("status")
    public ResponseSessionDTO status(@RequestBody @Valid StatusOperationRequest request) {
        return service.status(request);
    }

    @GetMapping("{id}")
    public ResponseSessionDTO get(@PathVariable Integer id) {
        return service.get(id);
    }
}

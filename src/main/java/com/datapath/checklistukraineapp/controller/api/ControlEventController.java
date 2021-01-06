package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlEventDTO;
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
    public void create(@RequestBody @Valid CreateControlEventRequest request) {
        service.create(request);
    }

    @GetMapping("{id}")
    public ControlEventDTO get(@PathVariable Long id) {
        return service.get(id);
    }
}

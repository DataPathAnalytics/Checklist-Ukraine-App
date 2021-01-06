package com.datapath.checklistukraineapp.controller.api;

import com.datapath.checklistukraineapp.dto.ControlObjectDTO;
import com.datapath.checklistukraineapp.service.ControlObjectWebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("control-objects")
public class ControlObjectController {

    private final ControlObjectWebService service;

    @GetMapping
    public List<ControlObjectDTO> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public ControlObjectDTO getById(@PathVariable String id) {
        return service.getById(id);
    }
}

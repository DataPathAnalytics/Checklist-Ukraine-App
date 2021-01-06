package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import com.datapath.checklistukraineapp.dao.service.ControlObjectDaoService;
import com.datapath.checklistukraineapp.dto.ControlObjectDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ControlObjectWebService {

    private final ControlObjectDaoService service;

    public List<ControlObjectDTO> list() {
        return service.findAll().stream()
                .map(c -> new ControlObjectDTO(c.getControlObjectId(), c.getName()))
                .collect(toList());
    }

    public ControlObjectDTO getById(String id) {
        ControlObjectEntity entity = service.findById(id);
        return new ControlObjectDTO(entity.getControlObjectId(), entity.getName());
    }
}

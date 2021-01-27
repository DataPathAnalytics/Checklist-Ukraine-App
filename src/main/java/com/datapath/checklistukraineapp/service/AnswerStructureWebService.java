package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistukraineapp.dto.AnswerStructureDTO;
import com.datapath.checklistukraineapp.util.DtoEntityConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AnswerStructureWebService {

    private final AnswerStructureDaoService service;

    public List<AnswerStructureDTO> list() {
        return service.findAll().stream()
                .map(DtoEntityConverter::map)
                .collect(toList());
    }
}

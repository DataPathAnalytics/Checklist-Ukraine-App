package com.datapath.checklistapp.controller.api;

import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.service.AnswerStructureWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("answer-structures")
@AllArgsConstructor
@Api(value = "Operation with answer structures")
public class AnswerStructureController {

    private final AnswerStructureWebService service;

    @ApiOperation(value = "list of available answer structures", response = AnswerStructureDTO.class)
    @GetMapping
    public List<AnswerStructureDTO> list() {
        return service.list();
    }
}

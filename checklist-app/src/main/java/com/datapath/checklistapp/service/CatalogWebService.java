package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CatalogWebService {

    private final QuestionSourceDaoService questionSourceService;

    public List<QuestionSourceDTO> getQuestionSources() {
        return questionSourceService.findAll().stream()
                .map(q -> {
                    QuestionSourceDTO dto = new QuestionSourceDTO();

                    BeanUtils.copyProperties(q, dto);

                    return dto;
                })
                .collect(toList());
    }
}

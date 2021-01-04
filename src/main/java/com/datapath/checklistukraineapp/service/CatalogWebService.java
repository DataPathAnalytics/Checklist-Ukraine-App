package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistukraineapp.dto.QuestionSourceDTO;
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

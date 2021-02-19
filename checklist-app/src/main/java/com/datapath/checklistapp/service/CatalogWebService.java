package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistapp.dao.service.InterpretationDaoService;
import com.datapath.checklistapp.dao.service.KnowledgeCategoryDaoService;
import com.datapath.checklistapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.IdValueDTO;
import com.datapath.checklistapp.dto.QuestionSourceDTO;
import com.datapath.checklistapp.service.converter.structure.AnswerConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CatalogWebService {

    private final QuestionSourceDaoService questionSourceService;
    private final KnowledgeCategoryDaoService knowledgeCategoryService;
    private final InterpretationDaoService interpretationService;
    private final AnswerStructureDaoService answerStructureService;
    private final AnswerConverter answerConverter;

    public List<QuestionSourceDTO> getQuestionSources() {
        return questionSourceService.findAll().stream()
                .map(q -> {
                    QuestionSourceDTO dto = new QuestionSourceDTO();

                    BeanUtils.copyProperties(q, dto);

                    return dto;
                })
                .collect(toList());
    }

    public List<IdValueDTO> getKnowledgeCategories() {
        return knowledgeCategoryService.findAll().stream()
                .map(k -> new IdValueDTO(k.getId(), k.getName()))
                .collect(toList());
    }

    public List<IdValueDTO> getInterpretations() {
        return interpretationService.findAll().stream()
                .map(i -> new IdValueDTO(i.getId(), i.getInterpretationValue()))
                .collect(toList());
    }

    public List<AnswerStructureDTO> getAnswerStructures() {
        return answerStructureService.findAll().stream()
                .map(answerConverter::map)
                .collect(toList());
    }

    public List<AnswerStructureDTO> searchAnswerStructures(String name) {
        return answerStructureService.searchByName(name).stream()
                .map(answerConverter::map)
                .collect(toList());
    }

    public List<IdValueDTO> searchKnowledgeCategories(String name) {
        return knowledgeCategoryService.searchByName(name).stream()
                .map(k -> new IdValueDTO(k.getId(), k.getName()))
                .collect(toList());
    }
}

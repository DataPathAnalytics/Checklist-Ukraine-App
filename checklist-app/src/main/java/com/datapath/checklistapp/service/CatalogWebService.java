package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistapp.dto.AnswerStructureDTO;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.converter.structure.AnswerConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CatalogWebService {

    private final AnswerStructureDaoService answerStructureService;
    private final AnswerConverter answerConverter;

    public List<AnswerStructureDTO> getAnswerStructures() {
        return answerStructureService.findAll().stream()
                .map(answerConverter::map)
                .collect(toList());
    }

    public PageableResponse<AnswerStructureDTO> searchAnswerStructures(SearchRequest request) {
        Page<AnswerStructureEntity> page = answerStructureService.searchByName(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(answerConverter::map)
                        .collect(toList())
        );
    }
}

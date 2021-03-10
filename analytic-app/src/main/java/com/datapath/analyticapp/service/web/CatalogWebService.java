package com.datapath.analyticapp.service.web;

import com.datapath.analyticapp.dao.entity.InterpretationEntity;
import com.datapath.analyticapp.dao.entity.KnowledgeCategoryEntity;
import com.datapath.analyticapp.dao.repository.InterpretationRepository;
import com.datapath.analyticapp.dao.repository.KnowledgeCategoryRepository;
import com.datapath.analyticapp.dto.InterpretationDTO;
import com.datapath.analyticapp.dto.KnowledgeCategoryDTO;
import com.datapath.analyticapp.dto.request.SearchRequest;
import com.datapath.analyticapp.dto.response.PageableResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.datapath.analyticapp.Constants.SEARCH_PATTERN;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CatalogWebService {

    private final InterpretationRepository interpretationRepository;
    private final KnowledgeCategoryRepository knowledgeCategoryRepository;


    public PageableResponse<KnowledgeCategoryDTO> searchKnowledgeCategories(SearchRequest request) {
        Page<KnowledgeCategoryEntity> page = knowledgeCategoryRepository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()), PageRequest.of(request.getPage(), request.getSize()));

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(k -> new KnowledgeCategoryDTO(k.getId(), k.getValue(), k.getName()))
                        .collect(toList())
        );
    }

    public PageableResponse<InterpretationDTO> searchInterpretations(SearchRequest request) {
        Page<InterpretationEntity> page = interpretationRepository.findByInterpretationMatchesRegexOrderByInterpretation(
                String.format(SEARCH_PATTERN, request.getKeyword()), PageRequest.of(request.getPage(), request.getSize()));

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(i -> new InterpretationDTO(i.getId(), i.getInterpretation()))
                        .collect(toList())
        );
    }
}

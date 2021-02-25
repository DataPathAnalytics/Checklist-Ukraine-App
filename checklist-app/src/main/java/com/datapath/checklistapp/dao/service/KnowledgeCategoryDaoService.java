package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.KnowledgeCategoryEntity;
import com.datapath.checklistapp.dao.repository.KnowledgeCategoryRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.checklistapp.util.Constants.SEARCH_PATTERN;

@Service
@AllArgsConstructor
public class KnowledgeCategoryDaoService {

    private final KnowledgeCategoryRepository repository;

    public List<KnowledgeCategoryEntity> findAll() {
        return repository.findAll();
    }

    public KnowledgeCategoryEntity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Node.KnowledgeCategory.name(), id));
    }

    public List<KnowledgeCategoryEntity> findByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public Page<KnowledgeCategoryEntity> searchByName(SearchRequest request) {
        return repository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

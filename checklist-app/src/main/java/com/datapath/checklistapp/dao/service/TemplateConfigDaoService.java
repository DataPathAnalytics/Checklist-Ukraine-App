package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateType;
import com.datapath.checklistapp.dao.repository.TemplateConfigRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.checklistapp.util.Constants.SEARCH_PATTERN;

@Service
@AllArgsConstructor
public class TemplateConfigDaoService {

    private final TemplateConfigRepository repository;

    public void save(TemplateConfigEntity entity) {
        repository.save(entity);
    }

    public List<TemplateConfigEntity> findAll() {
        return repository.findAll();
    }

    public List<TemplateConfigEntity> findAllByTemplateType(TemplateType type) {
        return repository.findAllByType(type);
    }

    public TemplateConfigEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateConfig", id));
    }

    public Page<TemplateConfigEntity> searchByName(SearchRequest request) {
        return repository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

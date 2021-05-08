package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dao.repository.AnswerStructureRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnswerStructureDaoService {

    private final AnswerStructureRepository repository;

    public AnswerStructureEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.AnswerStructure.name(), id));
    }

    public Page<AnswerStructureEntity> searchByName(SearchRequest request) {
        return repository.searchByName(
                request.getKeyword(), PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public AnswerStructureEntity findDefault() {
        return repository.findFirstBy_defaultIsTrue();
    }
}

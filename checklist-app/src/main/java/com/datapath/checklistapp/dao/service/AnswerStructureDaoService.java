package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.AnswerStructureEntity;
import com.datapath.checklistapp.dao.repository.AnswerStructureRepository;
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
public class AnswerStructureDaoService {

    private final AnswerStructureRepository repository;

    public AnswerStructureEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.AnswerStructure.name(), id));
    }

    public List<AnswerStructureEntity> findAll() {
        return repository.findAll();
    }

    public Page<AnswerStructureEntity> searchByName(SearchRequest request) {
        return repository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

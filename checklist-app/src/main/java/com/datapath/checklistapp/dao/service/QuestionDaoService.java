package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.repository.QuestionRepository;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionDaoService {

    private final QuestionRepository repository;

    public void save(QuestionEntity entity) {
        repository.save(entity);
    }

    public Page<QuestionEntity> findAll(PageableRequest request) {
        return repository.findAll(PageRequest.of(request.getPage(), request.getSize()));
    }

    public QuestionEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.Question.name(), id));
    }

    public List<QuestionEntity> findById(List<Integer> ids) {
        return repository.findAllById(ids);
    }

    public Page<QuestionEntity> searchByValue(SearchRequest request) {
        return repository.searchByValue(
                request.getKeyword(),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public Page<QuestionEntity> searchWithIdentifierByValue(SearchRequest request) {
        return repository.findByValueWithIdentifier(
                request.getKeyword(),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

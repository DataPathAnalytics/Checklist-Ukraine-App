package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.repository.QuestionRepository;
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
public class QuestionDaoService {

    private final QuestionRepository repository;

    public void save(QuestionEntity entity) {
        repository.save(entity);
    }

    public List<QuestionEntity> findAll() {
        return repository.findAll();
    }

    public QuestionEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("question", id));
    }

    public List<QuestionEntity> findById(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public List<Long> findByTemplateType(Integer templateTypeId) {
        return repository.getByTemplateType(templateTypeId);
    }

    public List<Long> findByQuestionType(Integer templateTypeId) {
        return repository.getByQuestionType(templateTypeId);
    }

    public Page<QuestionEntity> searchByName(SearchRequest request) {
        return repository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.repository.QuestionRepository;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.SEARCH_PATTERN;

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

    public QuestionEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.Question.name(), id));
    }

    public List<QuestionEntity> findById(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public Page<QuestionEntity> searchByValue(SearchRequest request) {
        return repository.findByValueMatchesRegexOrderByValue(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public List<QuestionEntity> findByDateCreated(LocalDateTime date, int limit) {
        return repository.findAllByDateCreatedAfterOrderByDateCreated(date, PageRequest.of(0, limit));
    }

    public List<Long> searchWithIdentifierByValue(SearchRequest request) {
        return repository.findWithIdentifier(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                request.getPage() * request.getSize(), request.getSize());
    }

    public Long countWithIdentifierByValue(SearchRequest request) {
        return repository.countIdentifier(String.format(SEARCH_PATTERN, request.getKeyword()));
    }
}

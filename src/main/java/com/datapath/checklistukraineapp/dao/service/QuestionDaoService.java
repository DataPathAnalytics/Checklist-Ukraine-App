package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.QuestionType;
import com.datapath.checklistukraineapp.dao.repository.QuestionRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<QuestionEntity> findByTypes(List<QuestionType> types) {
        return repository.findAllByTypeIn(types);
    }

    public QuestionEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("question", id));
    }

    public List<QuestionEntity> findByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }
}

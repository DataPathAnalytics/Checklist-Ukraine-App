package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.QuestionDomain;
import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.repository.QuestionRepository;
import com.datapath.checklistukraineapp.exception.QuestionException;
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

    public QuestionEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new QuestionException(id));
    }

    public List<QuestionDomain> findByTemplateId(Long templateId) {
        return repository.findQuestions(templateId);
    }
}

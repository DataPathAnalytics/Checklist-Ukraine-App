package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerClassifier;
import com.datapath.checklistukraineapp.dao.repository.AnswerRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerDaoService {

    private final AnswerRepository repository;

    public List<AnswerClassifier> findAll() {
        return repository.findAll();
    }

    public AnswerClassifier findById(Integer answerId) {
        return repository.findById(answerId).orElseThrow(() -> new EntityNotFoundException("answer", answerId));
    }
}

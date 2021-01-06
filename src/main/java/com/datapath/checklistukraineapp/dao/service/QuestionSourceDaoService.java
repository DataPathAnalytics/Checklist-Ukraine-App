package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistukraineapp.dao.repository.QuestionSourceRepository;
import com.datapath.checklistukraineapp.exception.QuestionSourceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionSourceDaoService {

    private final QuestionSourceRepository repository;

    public List<QuestionSourceEntity> findAll() {
        return repository.findAll();
    }

    public QuestionSourceEntity findById(String id) {
        return repository.findById(id).orElseThrow(() -> new QuestionSourceException(id));
    }
}

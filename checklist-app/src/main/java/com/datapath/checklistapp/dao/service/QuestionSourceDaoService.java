package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistapp.dao.repository.QuestionSourceRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.QuestionSource.name(), id));
    }
}

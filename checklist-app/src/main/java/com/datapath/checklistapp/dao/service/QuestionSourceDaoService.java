package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistapp.dao.repository.QuestionSourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionSourceDaoService {

    private final QuestionSourceRepository repository;

    public QuestionSourceEntity findByIdentifier(Integer identifier) {
        return repository.findByIdentifier(identifier);
    }
}

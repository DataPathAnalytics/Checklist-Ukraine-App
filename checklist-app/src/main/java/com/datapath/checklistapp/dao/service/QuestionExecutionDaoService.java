package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionExecutionEntity;
import com.datapath.checklistapp.dao.repository.QuestionExecutionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionExecutionDaoService {

    private final QuestionExecutionRepository repository;

    public void delete(QuestionExecutionEntity entity) {
        repository.delete(entity);
    }
}

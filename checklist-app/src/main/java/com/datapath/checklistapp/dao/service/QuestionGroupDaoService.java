package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.QuestionGroupEntity;
import com.datapath.checklistapp.dao.repository.QuestionGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionGroupDaoService {

    private final QuestionGroupRepository repository;

    public void delete(QuestionGroupEntity entity) {
        repository.delete(entity);
    }
}

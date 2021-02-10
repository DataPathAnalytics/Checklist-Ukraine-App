package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.QuestionType;
import com.datapath.checklistapp.dao.repository.classifier.QuestionTypeRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionTypeDaoService {

    private final QuestionTypeRepository repository;

    public List<QuestionType> findAll() {
        return repository.findAll();
    }

    public QuestionType findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("questionType", id));
    }
}

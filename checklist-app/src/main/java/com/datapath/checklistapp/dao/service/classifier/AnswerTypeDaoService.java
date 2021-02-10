package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.AnswerType;
import com.datapath.checklistapp.dao.repository.classifier.AnswerTypeRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerTypeDaoService {

    private final AnswerTypeRepository repository;

    public List<AnswerType> findAll() {
        return repository.findAll();
    }

    public AnswerType findById(Integer answerId) {
        return repository.findById(answerId).orElseThrow(() -> new EntityNotFoundException("answerType", answerId));
    }
}

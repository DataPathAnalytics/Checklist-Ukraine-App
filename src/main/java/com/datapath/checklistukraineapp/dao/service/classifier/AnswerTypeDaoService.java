package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.AnswerType;
import com.datapath.checklistukraineapp.dao.repository.classifier.AnswerTypeRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
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

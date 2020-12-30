package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionDaoService {

    private final QuestionRepository repository;
}

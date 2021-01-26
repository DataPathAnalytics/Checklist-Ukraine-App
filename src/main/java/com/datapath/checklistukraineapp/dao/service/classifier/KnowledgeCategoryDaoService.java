package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.KnowledgeCategory;
import com.datapath.checklistukraineapp.dao.repository.classifier.KnowledgeCategoryRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KnowledgeCategoryDaoService {

    private final KnowledgeCategoryRepository repository;

    public List<KnowledgeCategory> findAll() {
        return repository.findAll();
    }

    public KnowledgeCategory findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("knowledgeCategory", id));
    }
}

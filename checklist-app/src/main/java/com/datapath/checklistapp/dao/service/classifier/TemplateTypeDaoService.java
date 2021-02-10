package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.TemplateType;
import com.datapath.checklistapp.dao.repository.classifier.TemplateTypeRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemplateTypeDaoService {

    private final TemplateTypeRepository repository;

    public List<TemplateType> findAll() {
        return repository.findAll();
    }

    public TemplateType findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateType", id));
    }
}

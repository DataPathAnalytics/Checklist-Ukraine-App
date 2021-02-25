package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import com.datapath.checklistapp.dao.repository.classifier.TemplateConfigTypeRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TemplateConfigTypeDaoService {

    private final TemplateConfigTypeRepository repository;

    public List<TemplateConfigType> findAll() {
        return repository.findAll();
    }

    public TemplateConfigType findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.TemplateConfigType.name(), id));
    }
}

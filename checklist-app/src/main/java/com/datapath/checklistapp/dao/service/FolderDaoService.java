package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistapp.dao.repository.TemplateConfigFolderRepository;
import com.datapath.checklistapp.dao.repository.TemplateFolderRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FolderDaoService {

    private final TemplateFolderRepository templateRepository;
    private final TemplateConfigFolderRepository templateConfigRepository;

    public void save(TemplateFolderEntity entity) {
        templateRepository.save(entity);
    }

    public List<TemplateFolderEntity> findAllTemplateFolders() {
        return templateRepository.findAll();
    }

    public TemplateFolderEntity findTemplateFolderById(Integer id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Entity.TemplateFolder.name(), id));
    }

    public void save(TemplateConfigFolderEntity entity) {
        templateConfigRepository.save(entity);
    }

    public List<TemplateConfigFolderEntity> findAllTemplateConfigFolders() {
        return templateConfigRepository.findAll();
    }

    public TemplateConfigFolderEntity findTemplateConfigFolderById(Integer id) {
        return templateConfigRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Entity.TemplateConfigFolder.name(), id));
    }

    public Page<TemplateConfigFolderEntity> searchTemplateConfigFolders(SearchRequest request) {
        return templateConfigRepository.searchByName(
                request.getKeyword(), PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public Page<TemplateFolderEntity> searchTemplateFolders(SearchRequest request) {
        return templateRepository.searchByName(
                request.getKeyword(), PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

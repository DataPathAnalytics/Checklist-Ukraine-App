package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistapp.dao.repository.TemplateConfigFolderRepository;
import com.datapath.checklistapp.dao.repository.TemplateFolderRepository;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.checklistapp.util.Constants.SEARCH_PATTERN;

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

    public TemplateFolderEntity findTemplateFolderById(Long id) {
        return templateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateFolder", id));
    }

    public void save(TemplateConfigFolderEntity entity) {
        templateConfigRepository.save(entity);
    }

    public List<TemplateConfigFolderEntity> findAllTemplateConfigFolders() {
        return templateConfigRepository.findAll();
    }

    public TemplateConfigFolderEntity findTemplateConfigFolderById(Long id) {
        return templateConfigRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("templateConfigFolder", id));
    }

    public Page<TemplateConfigFolderEntity> searchTemplateConfigFolders(SearchRequest request) {
        return templateConfigRepository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }

    public Page<TemplateFolderEntity> searchTemplateFolders(SearchRequest request) {
        return templateRepository.findByNameMatchesRegexOrderByName(
                String.format(SEARCH_PATTERN, request.getKeyword()),
                PageRequest.of(request.getPage(), request.getSize())
        );
    }
}

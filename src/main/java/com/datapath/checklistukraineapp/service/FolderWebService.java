package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.service.FolderDaoService;
import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.request.folder.CreateFolderRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FolderWebService {

    private final FolderDaoService templateFolderService;

    @Transactional
    public void createTemplateFolder(CreateFolderRequest request) {
        TemplateFolderEntity entity = new TemplateFolderEntity();
        entity.setName(request.getName());
        templateFolderService.save(entity);
    }

    @Transactional
    public void createTemplateConfigFolder(CreateFolderRequest request) {
        TemplateConfigFolderEntity entity = new TemplateConfigFolderEntity();
        entity.setName(request.getName());
        templateFolderService.save(entity);
    }

    public List<FolderDTO> getTemplateFolders() {
        return templateFolderService.findAllTemplateFolders().stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                })
                .sorted(Comparator.comparing(FolderDTO::getDateCreated)
                        .thenComparing(FolderDTO::getName))
                .collect(toList());
    }

    public List<FolderDTO> getTemplateConfigFolders() {
        return templateFolderService.findAllTemplateConfigFolders().stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                })
                .sorted(Comparator.comparing(FolderDTO::getDateCreated)
                        .thenComparing(FolderDTO::getName))
                .collect(toList());
    }
}

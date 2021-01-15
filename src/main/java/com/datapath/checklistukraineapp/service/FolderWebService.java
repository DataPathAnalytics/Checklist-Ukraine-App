package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.service.TemplateFolderDaoService;
import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.request.CreateFolderRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FolderWebService {

    private final TemplateFolderDaoService templateFolderService;

    @Transactional
    public FolderDTO create(CreateFolderRequest request) {
        TemplateFolderEntity entity = new TemplateFolderEntity();
        entity.setName(request.getName());
        TemplateFolderEntity savedEntity = templateFolderService.save(entity);
        FolderDTO dto = new FolderDTO();
        BeanUtils.copyProperties(savedEntity, dto);
        return dto;
    }

    public List<FolderDTO> list() {
        return templateFolderService.findAll().stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();
                    BeanUtils.copyProperties(f, dto);
                    return dto;
                })
                .collect(toList());
    }
}

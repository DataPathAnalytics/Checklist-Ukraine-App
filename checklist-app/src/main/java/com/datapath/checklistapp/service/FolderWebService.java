package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistapp.dao.service.FolderDaoService;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.request.folder.CreateFolderRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.search.SearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
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

    public SearchResponse<FolderDTO> searchTemplateConfigFolders(SearchRequest request) {
        Page<TemplateConfigFolderEntity> page = templateFolderService.searchTemplateConfigFolders(request);

        return new SearchResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(f -> {
                            FolderDTO dto = new FolderDTO();
                            BeanUtils.copyProperties(f, dto);
                            return dto;
                        })
                        .collect(toList())
        );
    }

    public SearchResponse<FolderDTO> searchTemplateFolders(SearchRequest request) {
        Page<TemplateFolderEntity> page = templateFolderService.searchTemplateFolders(request);

        return new SearchResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(f -> {
                            FolderDTO dto = new FolderDTO();
                            BeanUtils.copyProperties(f, dto);
                            return dto;
                        })
                        .collect(toList())
        );
    }
}

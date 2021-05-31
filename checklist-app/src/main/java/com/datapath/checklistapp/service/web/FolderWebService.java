package com.datapath.checklistapp.service.web;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistapp.dao.service.FolderDaoService;
import com.datapath.checklistapp.dto.FolderDTO;
import com.datapath.checklistapp.dto.request.folder.FolderCreateRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FolderWebService {

    private final FolderDaoService templateFolderService;

    @Transactional
    public void createTemplateFolder(FolderCreateRequest request) {
        TemplateFolderEntity entity = new TemplateFolderEntity();
        entity.setName(request.getName());
        templateFolderService.save(entity);
    }

    @Transactional
    public void createTemplateConfigFolder(FolderCreateRequest request) {
        TemplateConfigFolderEntity entity = new TemplateConfigFolderEntity();
        entity.setName(request.getName());
        templateFolderService.save(entity);
    }

    public PageableResponse<FolderDTO> searchTemplateConfigFolders(SearchRequest request) {
        Page<TemplateConfigFolderEntity> page = templateFolderService.searchTemplateConfigFolders(request);

        return new PageableResponse<>(
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

    public PageableResponse<FolderDTO> searchTemplateFolders(SearchRequest request) {
        Page<TemplateFolderEntity> page = templateFolderService.searchTemplateFolders(request);

        return new PageableResponse<>(
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

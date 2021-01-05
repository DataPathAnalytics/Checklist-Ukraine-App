package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.service.TemplateFolderDaoService;
import com.datapath.checklistukraineapp.dto.FolderDTO;
import com.datapath.checklistukraineapp.dto.request.CreateFolderRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FolderWebService {

    private final TemplateFolderDaoService templateFolderService;

    @Transactional
    public void create(CreateFolderRequest request) {

        TemplateFolderEntity entity = new TemplateFolderEntity();
        entity.setName(request.getName());

        LocalDate now = LocalDate.now();

        entity.setDateCreated(now);
        entity.setDateModified(now);

        if (nonNull(request.getParentId())) {
            TemplateFolderEntity parent = templateFolderService.findById(request.getParentId());
            parent.getChildren().add(entity);
            templateFolderService.save(parent);
        } else {
            entity.setRoot(true);
            templateFolderService.save(entity);
        }
    }

    public List<FolderDTO> list() {
        return templateFolderService.findAll().stream()
                .map(f -> {
                    FolderDTO dto = new FolderDTO();

                    BeanUtils.copyProperties(f, dto);

                    dto.setChildren(
                            f.getChildren().stream()
                                    .map(TemplateFolderEntity::getId)
                                    .collect(toList())
                    );

                    return dto;
                })
                .collect(toList());
    }
}

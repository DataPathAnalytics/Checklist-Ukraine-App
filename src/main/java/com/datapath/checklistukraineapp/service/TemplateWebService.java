package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.TemplateEntity;
import com.datapath.checklistukraineapp.dao.entity.TemplateFolderEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.service.TemplateDaoService;
import com.datapath.checklistukraineapp.dao.service.TemplateFolderDaoService;
import com.datapath.checklistukraineapp.dao.service.UserDaoService;
import com.datapath.checklistukraineapp.domain.dto.TemplateDTO;
import com.datapath.checklistukraineapp.domain.request.template.CreateTemplateRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.datapath.checklistukraineapp.util.UserUtils.getCurrentUserId;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class TemplateWebService {

    private final TemplateDaoService templateService;
    private final UserDaoService userService;
    private final TemplateFolderDaoService templateFolderService;

    @Transactional
    public void save(CreateTemplateRequest request) {
        UserEntity author = userService.findById(getCurrentUserId());
        TemplateFolderEntity folder = templateFolderService.findById(request.getFolderId());

        TemplateEntity entity = new TemplateEntity();

        entity.setName(request.getName());
        entity.setDateCreated(LocalDate.now());
        entity.setAuthor(author);
        entity.setFolder(folder);

        templateService.save(entity);
    }

    public List<TemplateDTO> list() {
        List<TemplateEntity> all = templateService.findAll();
        return all.stream()
                .map(t -> {
                    TemplateDTO dto = new TemplateDTO();
                    BeanUtils.copyProperties(t, dto);
                    dto.setAuthorId(t.getAuthor().getId());
                    dto.setFolderId(t.getFolder().getId());
                    return dto;
                })
                .collect(toList());
    }

    public TemplateDTO get(Long id) {
        TemplateEntity entity = templateService.findById(id);
        TemplateDTO dto = new TemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setFolderId(entity.getFolder().getId());
        return dto;
    }
}

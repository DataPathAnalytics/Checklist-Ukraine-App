package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import com.datapath.checklistukraineapp.dao.entity.ControlObjectEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlStatusEntity;
import com.datapath.checklistukraineapp.dao.entity.classifier.ControlTypeEntity;
import com.datapath.checklistukraineapp.dao.service.*;
import com.datapath.checklistukraineapp.dto.ControlEventDTO;
import com.datapath.checklistukraineapp.dto.TemplateDTO;
import com.datapath.checklistukraineapp.dto.request.event.CreateControlEventRequest;
import com.datapath.checklistukraineapp.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.datapath.checklistukraineapp.util.Constants.IN_PROCESS_CONTROL_STATUS;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ControlEventWebService {

    private final ControlEventDaoService controlEventService;
    private final UserDaoService userService;
    private final ControlObjectDaoService controlObjectService;
    private final ControlTypeDaoService controlTypeService;
    private final ControlStatusDaoService controlStatusService;
    private final TemplateDaoService templateService;

    public List<ControlEventDTO> list() {
        return controlEventService.findAll().stream()
                .map(c -> {
                    ControlEventDTO dto = new ControlEventDTO();

                    BeanUtils.copyProperties(c, dto);

                    return dto;
                }).collect(toList());
    }

    @Transactional
    public void create(CreateControlEventRequest request) {
        Long currentUserId = UserUtils.getCurrentUserId();
        UserEntity user = userService.findById(currentUserId);
        ControlObjectEntity object = controlObjectService.findById(request.getControlObjectId());
        ControlTypeEntity type = controlTypeService.findById(request.getControlTypeId());
        ControlStatusEntity status = controlStatusService.findById(IN_PROCESS_CONTROL_STATUS);

        ControlEventEntity event = new ControlEventEntity();
        event.setName(request.getName());
        event.setStatus(status);

        event.setAuthor(user);
        event.setObject(object);
        event.setType(type);

        if (!isEmpty(request.getMemberIds())) {
            event.setMembers(userService.findByIds(request.getMemberIds()));
        }

        if (!isEmpty(request.getTemplateIds())) {
            event.setTemplates(templateService.findByIds(request.getTemplateIds()));
        }

        controlEventService.save(event);
    }

    public ControlEventDTO get(Long id) {
        ControlEventEntity event = controlEventService.findById(id);

        ControlEventDTO response = new ControlEventDTO();

        BeanUtils.copyProperties(event, response);
        response.setAuthorId(event.getAuthor().getId());
        response.setControlTypeId(event.getType().getControlTypeId());
        response.setControlObjectId(event.getObject().getControlObjectId());
        response.setControlObjectName(event.getObject().getName());
        response.setControlStatusId(event.getStatus().getControlStatusId());

        response.setTemplates(
                event.getTemplates().stream()
                        .map(t -> {
                            TemplateDTO dto = new TemplateDTO();
                            dto.setId(t.getId());
                            dto.setAuthorId(t.getAuthor().getId());
                            dto.setFolderId(t.getFolder().getId());
                            dto.setDateCreated(t.getDateCreated());
                            dto.setName(t.getName());
                            return dto;
                        })
                        .collect(toList())
        );

        return response;
    }
}

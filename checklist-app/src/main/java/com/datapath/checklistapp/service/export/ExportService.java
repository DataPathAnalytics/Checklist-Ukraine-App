package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.entity.classifier.Permission;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dao.service.classifier.PermissionDaoService;
import com.datapath.checklistapp.dto.response.export.*;
import com.datapath.checklistapp.service.converter.structure.ResponseSessionConverter;
import com.datapath.checklistapp.service.converter.structure.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.AUDITOR_ROLE;
import static com.datapath.checklistapp.util.Constants.IN_COMPLETED_STATUS;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ExportService {

    private final ResponseSessionConverter responseSessionConverter;
    private final ControlActivityDaoService controlActivityService;
    private final ResponseSessionDaoService responseSessionService;
    private final UserDaoService userService;
    private final UserConverter userConverter;
    private final PermissionDaoService permissionService;

    public ExportUserResponse getUpdateUsers(LocalDateTime date, int limit) {
        Permission auditorPermission = permissionService.findByRole(AUDITOR_ROLE);

        List<UserEntity> users = userService.findUpdatedUsers(date, limit).stream()
                .filter(u -> u.getPermissions().contains(auditorPermission))
                .collect(toList());

        ExportUserResponse response = new ExportUserResponse();
        if (isEmpty(users)) return response;

        response.setUsers(users.stream().map(userConverter::mapExport).collect(toList()));
        response.setNextOffset(users.stream().map(UserEntity::getDateModified).max(LocalDateTime::compareTo).get());

        return response;
    }

    @Transactional
    public ExportChecklistDateResponse getResponseSessionDates(LocalDateTime offset, int limit) {
        ExportChecklistDateResponse response = new ExportChecklistDateResponse();

        List<ExportSessionResponseDomain> sessions = responseSessionService.getResponseSessionDates(offset, limit);

        if (isEmpty(sessions)) return response;

        response.setData(
                sessions.stream()
                        .map(s -> {
                            ExportChecklistDateDTO dto = new ExportChecklistDateDTO();
                            dto.setId(s.getId());
                            dto.setDateModified(s.getDateModified());
                            return dto;
                        })
                        .collect(toList())
        );

        response.setNextOffset(
                sessions.stream()
                        .map(ExportSessionResponseDomain::getDateModified)
                        .max(LocalDateTime::compareTo)
                        .get()
        );

        return response;
    }

    @Transactional
    public ExportChecklistDateResponse getControlActivityDates(LocalDateTime offset, int limit) {
        ExportChecklistDateResponse response = new ExportChecklistDateResponse();

        List<ExportControlActivityDomain> sessions = controlActivityService.getControlActivityDates(offset, limit);

        if (isEmpty(sessions)) return response;

        response.setData(
                sessions.stream()
                        .map(e -> {
                            ExportChecklistDateDTO dto = new ExportChecklistDateDTO();
                            dto.setId(e.getId());
                            dto.setDateModified(e.getDateModified());
                            return dto;
                        })
                        .collect(toList())
        );

        response.setNextOffset(
                sessions.stream()
                        .map(ExportControlActivityDomain::getDateModified)
                        .max(LocalDateTime::compareTo)
                        .get()
        );

        return response;
    }

    @Transactional
    public ExportControlActivityDTO getControlActivity(Integer id) {
        ExportControlActivityDTO response = new ExportControlActivityDTO();

        ControlActivityEntity controlActivity = controlActivityService.findById(id);

        response.setId(controlActivity.getId());
        response.setActivity(responseSessionConverter.map(controlActivity.getActivityResponse()));
        response.setSessions(controlActivity.getSessionResponses()
                .stream()
                .filter(s -> IN_COMPLETED_STATUS.equals(s.getStatus().getId()))
                .map(responseSessionConverter::map)
                .collect(toList())
        );

        return response;
    }

    @Transactional
    public ExportResponseSessionDTO getResponseSession(Integer id) {
        ExportResponseSessionDTO response = new ExportResponseSessionDTO();
        SessionEntity session = responseSessionService.findById(id);
        response.setControlActivityId(session.getActivity().getId());
        response.setSession(responseSessionConverter.map(session));
        return response;
    }
}

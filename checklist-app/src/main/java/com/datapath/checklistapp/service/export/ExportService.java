package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dto.response.export.*;
import com.datapath.checklistapp.service.converter.structure.ResponseSessionConverter;
import com.datapath.checklistapp.service.converter.structure.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.datapath.checklistapp.util.Constants.AUDITOR_ROLE;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ExportService {

    private static final List<String> EXPORT_USER_ROLES = Arrays.asList(AUDITOR_ROLE);

    private final ResponseSessionConverter responseSessionConverter;
    private final ControlActivityDaoService controlActivityService;
    private final ResponseSessionDaoService responseSessionService;
    private final UserDaoService userService;
    private final UserConverter userConverter;

    public ExportUserResponse getUpdateUsers(LocalDateTime date, int limit) {
        List<UserEntity> users = userService.findUpdatedUsers(date, limit).stream()
                .filter(u -> EXPORT_USER_ROLES.contains(u.getPermission().getRole()))
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
    public ExportControlActivityDTO getControlActivity(Long id) {
        ExportControlActivityDTO response = new ExportControlActivityDTO();

        ControlActivityDomain controlActivity = controlActivityService.findById(id);

        response.setId(controlActivity.getId());
        response.setActivity(responseSessionConverter.map(responseSessionService.findById(controlActivity.getActivityResponseId())));
        response.setSessions(responseSessionService.findByIds(controlActivity.getSessionResponseIds())
                .stream()
                .map(responseSessionConverter::map)
                .collect(toList())
        );

        return response;
    }

    @Transactional
    public ExportResponseSessionDTO getResponseSession(Long id) {
        ExportResponseSessionDTO response = new ExportResponseSessionDTO();
        response.setControlActivityId(controlActivityService.getControlActivityIdBySessionId(id));
        response.setSession(responseSessionConverter.map(responseSessionService.findById(id)));
        return response;
    }
}

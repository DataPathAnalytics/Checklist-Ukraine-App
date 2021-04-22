package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dto.response.export.ExportQuestionResponse;
import com.datapath.checklistapp.dto.response.export.ExportSessionActivityDTO;
import com.datapath.checklistapp.dto.response.export.ExportSessionActivityResponse;
import com.datapath.checklistapp.dto.response.export.ExportUserResponse;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import com.datapath.checklistapp.service.converter.structure.ResponseSessionConverter;
import com.datapath.checklistapp.service.converter.structure.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.datapath.checklistapp.util.Constants.AUDITOR_ROLE;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ExportService {

    private static final List<String> EXPORT_USER_ROLES = Arrays.asList(AUDITOR_ROLE);

    private final QuestionDaoService questionService;
    private final QuestionConverter questionConverter;
    private final ResponseSessionConverter responseSessionConverter;
    private final ControlActivityDaoService controlActivityService;
    private final ResponseSessionDaoService responseSessionService;
    private final UserDaoService userService;
    private final UserConverter userConverter;

    public ExportQuestionResponse getNewQuestions(LocalDateTime date, int limit) {
        List<QuestionEntity> questions = questionService.findByDateCreated(date, limit);

        ExportQuestionResponse response = new ExportQuestionResponse();
        if (isEmpty(questions)) return response;

        response.setQuestions(questions.stream().map(questionConverter::map).collect(toList()));
        response.setNextOffset(
                questions.stream()
                        .map(QuestionEntity::getDateCreated)
                        .max(LocalDateTime::compareTo)
                        .get()
        );

        return response;
    }

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
    public ExportSessionActivityResponse getUpdatedSession(LocalDateTime offset, int limit) {
        ExportSessionActivityResponse response = new ExportSessionActivityResponse();

        List<ExportSessionResponseDomain> sessions = responseSessionService.findForExport(offset, limit);

        if (isEmpty(sessions)) return response;

        Map<Long, List<Long>> activitySessions = sessions.stream()
                .collect(Collectors.groupingBy(
                        ExportSessionResponseDomain::getActivityId,
                        Collectors.mapping(ExportSessionResponseDomain::getSessionResponseId, Collectors.toList()))
                );

        response.setData(
                activitySessions.entrySet().stream()
                        .map(e -> {
                            ExportSessionActivityDTO dto = new ExportSessionActivityDTO();
                            dto.setId(e.getKey());
                            dto.setSessions(
                                    responseSessionService.findByIds(e.getValue()).stream()
                                            .map(responseSessionConverter::map)
                                            .collect(toList())
                            );
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
    public ExportSessionActivityResponse getUpdatedActivities(LocalDateTime offset, int limit) {
        ExportSessionActivityResponse response = new ExportSessionActivityResponse();

        List<ExportControlActivityDomain> sessions = controlActivityService.findForExport(offset, limit);

        if (isEmpty(sessions)) return response;

        response.setData(
                sessions.stream()
                        .map(e -> {
                            ExportSessionActivityDTO dto = new ExportSessionActivityDTO();
                            dto.setId(e.getId());
                            dto.setActivity(responseSessionConverter.map(responseSessionService.findById(e.getActivityResponseId())));
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
}

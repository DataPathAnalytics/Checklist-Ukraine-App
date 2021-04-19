package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.domain.ExportResponseSessionDomain;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dto.response.export.ExportControlActivityResponse;
import com.datapath.checklistapp.dto.response.export.ExportQuestionResponse;
import com.datapath.checklistapp.dto.response.export.ExportUserResponse;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
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

    @Transactional
    public ExportControlActivityResponse getUpdatedSession(LocalDateTime offset, int limit) {
        ExportControlActivityResponse response = new ExportControlActivityResponse();

        List<ExportResponseSessionDomain> domains = responseSessionService.getUpdateResponseSessions(offset, limit);

        Map<Long, List<ExportResponseSessionDomain>> activitySessionsMap = domains.stream()
                .collect(Collectors.groupingBy(ExportResponseSessionDomain::getActivityId));

        activitySessionsMap.forEach((k, v) -> {
            //TODO:needs complete logic
        });

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
}

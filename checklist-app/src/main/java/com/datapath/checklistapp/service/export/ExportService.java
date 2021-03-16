package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.service.ControlActivityDaoService;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dao.service.ResponseSessionDaoService;
import com.datapath.checklistapp.dao.service.UserDaoService;
import com.datapath.checklistapp.dto.response.export.*;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ExportService {

    private final QuestionDaoService questionService;
    private final QuestionConverter questionConverter;
    private final ControlActivityDaoService controlActivityService;
    private final ResponseSessionDaoService responseSessionService;
    private final UserDaoService userService;

    public ExportQuestionResponse getNewQuestions(LocalDateTime date, int limit) {
        List<QuestionEntity> questions = questionService.findByDateCreated(date, limit);

        if (isEmpty(questions)) return new ExportQuestionResponse();

        ExportQuestionResponse response = new ExportQuestionResponse();
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

        List<LocalDateTime> dates = new ArrayList<>();

        response.setActivities(controlActivityService.getUpdateControlActivities(offset).stream()
                .map(a -> {
                    ExportControlActivityDTO activity = new ExportControlActivityDTO();
                    activity.setId(a.getId());
                    activity.setAuthorId(a.getAuthorId());
                    activity.setMemberIds(a.getMemberIds());
                    activity.setName(a.getName());
                    activity.setDateCreated(a.getDateCreated());

                    activity.setSessions(responseSessionService.findByIds(a.getSessionsIds()).stream()
                            .map(s -> {
                                dates.add(s.getDateModified());

                                ExportSessionDTO session = new ExportSessionDTO();
                                session.setId(s.getId());
                                session.setName(s.getName());
                                session.setAuthorId(s.getAuthor().getId());
                                session.setDateCreated(s.getDateCreated());
                                session.setDateModified(s.getDateModified());
                                session.setReviewerId(s.getReviewer().getId());
                                session.setExecutions(
                                        s.getAnswers().stream()
                                                .map(answer -> questionConverter.map(answer.getQuestionExecution(), answer))
                                                .collect(toList())
                                );
                                return session;
                            })
                            .collect(toList())
                    );

                    return activity;
                })
                .collect(toList())
        );

        response.setNextOffset(dates.stream().max(LocalDateTime::compareTo).get());

        return response;
    }

    public List<ExportUserDTO> getUsers(int page, int size) {
        return userService.findAll(page, size)
                .stream()
                .map(u -> {
                    ExportUserDTO user = new ExportUserDTO();
                    user.setId(u.getId());
                    user.setEmail(u.getEmail());
                    user.setFirstName(u.getFirstName());
                    user.setLastName(u.getLastName());
                    return user;
                })
                .collect(toList());
    }
}

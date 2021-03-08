package com.datapath.checklistapp.service.export;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dto.response.export.ExportQuestionResponse;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class ExportService {

    private final QuestionDaoService questionService;
    private final QuestionConverter questionConverter;

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
}

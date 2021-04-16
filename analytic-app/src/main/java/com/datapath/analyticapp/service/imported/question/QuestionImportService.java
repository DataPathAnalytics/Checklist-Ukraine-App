package com.datapath.analyticapp.service.imported.question;

import com.datapath.analyticapp.dao.entity.QuestionEntity;
import com.datapath.analyticapp.dao.repository.QuestionRepository;
import com.datapath.analyticapp.dto.imported.question.QuestionApiResponse;
import com.datapath.analyticapp.service.imported.RestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class QuestionImportService {

    private static final int QUESTIONS_LIMIT = 10;

    @Value("${checklist.question.url}")
    private String apiUrl;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RestManager restManager;
    @Autowired
    private QuestionUpdateService questionUpdateService;

    public void updateQuestions() {
        String url = restManager.getUrlByOffset(apiUrl, getLastModifiedTenderDate(), QUESTIONS_LIMIT);

        QuestionApiResponse response;

        do {
            response = restManager.getData(url, QuestionApiResponse.class);

            if (isEmpty(response.getQuestions())) break;

            response.getQuestions().forEach(questionUpdateService::process);

            url = restManager.getUrlByOffset(apiUrl, response.getNextOffset(), QUESTIONS_LIMIT);

        } while (true);
    }

    private LocalDateTime getLastModifiedTenderDate() {
        Optional<QuestionEntity> lastQuestion = questionRepository.findFirstByDateCreatedNotNullOrderByDateCreatedDesc();
        return lastQuestion.map(QuestionEntity::getDateCreated).orElse(null);
    }
}

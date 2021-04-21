package com.datapath.analyticapp.service.imported.question;

import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import com.datapath.analyticapp.dao.repository.QuestionRepository;
import com.datapath.analyticapp.dto.imported.question.QuestionApiResponse;
import com.datapath.analyticapp.service.imported.ImportService;
import com.datapath.analyticapp.service.imported.RestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Order(2)
public class QuestionImportService implements ImportService {

    private static final int LIMIT = 10;

    @Value("${checklist.question.part.url}")
    private String apiUrlPart;

    @Autowired
    private QuestionRepository repository;
    @Autowired
    private RestManager restManager;
    @Autowired
    private QuestionUpdateService updateService;

    @Override
    public void update() {
        String url = restManager.getUrlByOffset(apiUrlPart, getLastModified(), LIMIT);

        QuestionApiResponse response;

        do {
            response = restManager.getData(url, QuestionApiResponse.class);

            if (isEmpty(response.getQuestions())) break;

            response.getQuestions().forEach(updateService::process);

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), LIMIT);

        } while (true);
    }

    @Override
    public LocalDateTime getLastModified() {
        Optional<QuestionEntity> lastQuestion = repository.findFirstByDateCreatedNotNullOrderByDateCreatedDesc();
        return lastQuestion.map(QuestionEntity::getDateCreated).orElse(null);
    }
}

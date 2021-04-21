package com.datapath.analyticapp.service.imported.session;

import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import com.datapath.analyticapp.dao.repository.ResponseSessionRepository;
import com.datapath.analyticapp.dto.imported.SessionActivityResponse;
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
@Order(4)
public class SessionImportService implements ImportService {

    private static final int LIMIT = 10;

    @Value("${checklist.sessions.part.url}")
    private String apiUrlPart;

    @Autowired
    private ResponseSessionRepository repository;
    @Autowired
    private RestManager restManager;
    @Autowired
    private SessionUpdateService updateService;

    @Override
    public void update() {
        String url = restManager.getUrlByOffset(apiUrlPart, getLastModified(), LIMIT);

        SessionActivityResponse response;

        do {
            response = restManager.getData(url, SessionActivityResponse.class);

            if (isEmpty(response.getData())) break;

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), LIMIT);

        } while (true);
    }

    @Override
    public LocalDateTime getLastModified() {
        Optional<ResponseSessionEntity> last = repository.findFirstByDateModifiedNotNullOrderByDateModifiedDesc();
        return last.map(ResponseSessionEntity::getDateModified).orElse(null);
    }
}

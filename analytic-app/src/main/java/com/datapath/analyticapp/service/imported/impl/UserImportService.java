package com.datapath.analyticapp.service.imported.impl;

import com.datapath.analyticapp.dao.entity.imported.UserEntity;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dto.imported.user.UserApiResponse;
import com.datapath.analyticapp.service.imported.ImportService;
import com.datapath.analyticapp.service.imported.RestManager;
import com.datapath.analyticapp.service.miner.handler.UserUpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Order(1)
public class UserImportService implements ImportService {

    private static final int LIMIT = 10;

    @Value("${checklist.user.part.url}")
    private String apiUrlPart;

    @Autowired
    private RestManager restManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUpdateHandler updateService;

    @Override
    public void upload() {
        String url = restManager.getUrlByOffset(apiUrlPart, getLastModified(), LIMIT);

        UserApiResponse response;

        do {
            response = restManager.getData(url, UserApiResponse.class);

            if (isEmpty(response.getUsers())) break;

            response.getUsers().forEach(updateService::update);

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), LIMIT);

        } while (true);
    }

    public LocalDateTime getLastModified() {
        Optional<UserEntity> lastUser = repository.findFirstByDateModifiedNotNullOrderByDateModifiedDesc();
        return lastUser.map(UserEntity::getDateModified).orElse(null);
    }
}

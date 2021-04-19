package com.datapath.analyticapp.service.imported.user;

import com.datapath.analyticapp.dao.entity.imported.UserEntity;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dto.imported.user.UserApiResponse;
import com.datapath.analyticapp.service.imported.RestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class UserImportService {

    private static final int USERS_LIMIT = 10;

    @Value("${checklist.user.part.url}")
    private String apiUrlPart;

    @Autowired
    private RestManager restManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUpdateService updateService;

    public void updateUsers() {
        String url = restManager.getUrlByOffset(apiUrlPart, getLastModifiedUserDate(), USERS_LIMIT);

        UserApiResponse response;

        do {
            response = restManager.getData(url, UserApiResponse.class);

            if (isEmpty(response.getUsers())) break;

            response.getUsers().forEach(updateService::process);

            url = restManager.getUrlByOffset(apiUrlPart, response.getNextOffset(), USERS_LIMIT);

        } while (true);
    }

    private LocalDateTime getLastModifiedUserDate() {
        Optional<UserEntity> lastUser = repository.findFirstByDateModifiedNotNullOrderByDateModifiedDesc();
        return lastUser.map(UserEntity::getDateModified).orElse(null);
    }
}

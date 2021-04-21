package com.datapath.analyticapp.service.imported.user;

import com.datapath.analyticapp.dao.entity.imported.UserEntity;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dto.imported.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserUpdateService {

    private final UserRepository repository;

    @Transactional
    public void process(UserDTO dto) {
        UserEntity entity = repository.findFirstByEmail(dto.getEmail()).orElseGet(UserEntity::new);

        entity.setOuterId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateModified(dto.getDateModified());
        entity.setRegisteredDate(dto.getRegisteredDate());

        repository.save(entity);
    }
}

package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.repository.UserRepository;
import com.datapath.checklistukraineapp.exception.UserException;
import com.datapath.checklistukraineapp.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDaoService {

    private final UserRepository repository;

    public void save(UserEntity user) {
        repository.save(user);
    }

    public UserEntity findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserEntity findActiveByEmail(String email) {
        return repository.findByEmailAndRemovedIsFalse(email);
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity findById(Long id) throws UserException {
        return repository.findById(id)
                .orElseThrow(() -> new UserException("User Not found"));
    }

    public UserEntity findAdmin() {
        return repository.findFirstByRole(UserRole.admin.getValue());
    }
}

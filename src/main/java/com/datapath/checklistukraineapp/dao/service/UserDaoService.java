package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.repository.UserRepository;
import com.datapath.checklistukraineapp.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public Page<UserEntity> findAll(int page, int size) {
        return repository.findAllByRemovedIsFalseAndRegisteredDateNotNullOrderByLockedDescRegisteredDateDescFirstNameAsc(
                PageRequest.of(page, size)
        );
    }

    public UserEntity findById(Long id) throws UserException {
        return repository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));
    }

    public UserEntity findAdmin() {
        return repository.findAdminUser();
    }

    public boolean existsNotChecked() {
        return repository.existsNotChecked();
    }

    public List<UserEntity> findByIds(Set<Long> ids) {
        return repository.findAllById(ids);
    }
}

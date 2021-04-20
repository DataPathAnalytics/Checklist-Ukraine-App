package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.entity.UserEntity;
import com.datapath.checklistapp.dao.repository.UserRepository;
import com.datapath.checklistapp.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
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
        return repository.findAllByRemovedIsFalseAndSuperAdminIsFalseOrderByLockedDescRegisteredDateDescFirstNameAsc(
                PageRequest.of(page, size)
        );
    }

    public UserEntity findById(Long id) throws UserException {
        return repository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));
    }

    public List<UserEntity> findAdmins() {
        return repository.findAdmins();
    }

    public boolean existsNotChecked() {
        return repository.existsNotChecked();
    }

    public Set<UserEntity> findByIds(List<Long> ids) {
        return new HashSet<>(repository.findAllById(ids));
    }

    public List<UserEntity> findUpdatedUsers(LocalDateTime date, int limit) {
        return repository.findAllByDateModifiedIsNotNullAndDateModifiedAfterOrderByDateModified(date, PageRequest.of(0, limit));
    }
}

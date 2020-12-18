package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.node.User;
import com.datapath.checklistukraineapp.dao.repository.UserRepository;
import com.datapath.checklistukraineapp.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDaoService {

    private final UserRepository repository;

    public void save(User user) {
        repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findActiveByEmail(String email) {
        return repository.findByEmailAndRemovedIsFalse(email);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) throws UserException {
        return repository.findById(id)
                .orElseThrow(() -> new UserException("User Not found"));
    }
}

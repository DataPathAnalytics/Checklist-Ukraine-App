package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.node.UserNode;
import com.datapath.checklistukraineapp.dao.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDaoService {

    private final UserRepository repository;

    public void save(UserNode user) {
        repository.save(user);
    }

    public UserNode findByEmail(String email) {
        return repository.findByEmail(email);
    }
}

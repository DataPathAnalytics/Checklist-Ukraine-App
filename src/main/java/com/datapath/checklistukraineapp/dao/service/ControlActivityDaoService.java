package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistukraineapp.dao.entity.UserEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlActivityRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ControlActivityDaoService {

    private final ControlActivityRepository repository;

    public List<ControlActivityEntity> findAllByUser(UserEntity author) {
//        return repository.findUserControlEvents(userId);
        return repository.findAllByAuthor(author);
    }

    public List<ControlActivityEntity> findAll() {
        return repository.findAll();
//        return repository.findControlEvents();
    }

    public ControlActivityEntity save(ControlActivityEntity entity) {
        return repository.save(entity);
    }

    public ControlActivityEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("controlEvent", id));
    }

    public Set<Long> findRelatedUsers(Long id) {
        return repository.findRelatedUsers(id);
    }
}

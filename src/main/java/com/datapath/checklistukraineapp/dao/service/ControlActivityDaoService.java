package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ControlActivityDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlActivityRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.util.database.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class ControlActivityDaoService {

    private final ControlActivityRepository repository;

    public List<ControlActivityDomain> findAll(Long userId) {
        if (nonNull(userId)) return repository.findUserControlActivities(userId);
        return repository.findControlActivities();
    }

    public ControlActivityEntity save(ControlActivityEntity entity) {
        return repository.save(entity);
    }

    public ControlActivityDomain findById(Long id) {
        return repository.findControlActivity(id).orElseThrow(() -> new EntityNotFoundException(Node.ControlActivity.name(), id));
    }

    public Set<Long> findRelatedUsers(Long id) {
        return repository.findRelatedUsers(id);
    }
}

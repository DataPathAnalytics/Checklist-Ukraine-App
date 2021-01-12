package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ControlEventDomain;
import com.datapath.checklistukraineapp.dao.entity.ControlEventEntity;
import com.datapath.checklistukraineapp.dao.repository.ControlEventRepository;
import com.datapath.checklistukraineapp.exception.ControlEventException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ControlEventDaoService {

    private final ControlEventRepository repository;

    public List<ControlEventDomain> findAll() {
        return repository.findControlEvents();
    }

    public Long save(ControlEventEntity entity) {
        return repository.save(entity).getId();
    }

    public ControlEventDomain findById(Long id) {
        return repository.findControlEvent(id).orElseThrow(() -> new ControlEventException(id));
    }

    public void createRelationshipWithUser(Long eventId, List<Long> userIds) {
        repository.createRelationshipWithUser(eventId, userIds);
    }

    public void createRelationshipWithTemplate(Long eventId, List<Long> templateIds) {
        repository.createRelationshipWithTemplate(eventId, templateIds);
    }

    public Set<Long> getMembers(Long eventId) {
        return repository.findMembers(eventId);
    }

    public void removeRelationshipWithStatus(Long id) {
        repository.removeRelationshipWithStatus(id);
    }

    public void createRelationshipWithStatus(Long id, Integer controlStatusId) {
        repository.createRelationshipWithStatus(id, controlStatusId);
    }

    public Set<Long> findLinkedUsers(Long eventId) {
        return repository.findLinkedUsers(eventId);
    }
}

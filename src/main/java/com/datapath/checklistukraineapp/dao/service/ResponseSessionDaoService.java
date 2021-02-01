package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistukraineapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistukraineapp.dao.repository.ResponseSessionRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResponseSessionDaoService {

    private final ResponseSessionRepository repository;

    public List<ResponseSessionDomain> findResponseSessionByActivityId(Long activityId) {
        return repository.findResponseSessionByActivityId(activityId);
    }

    public ResponseSessionEntity save(ResponseSessionEntity entity) {
        return repository.save(entity);
    }

    public ResponseSessionEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("checklist", id));
    }
}

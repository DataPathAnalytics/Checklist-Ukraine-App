package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.domain.ResponseSessionDomain;
import com.datapath.checklistapp.dao.entity.ResponseSessionEntity;
import com.datapath.checklistapp.dao.repository.ResponseSessionRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
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
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Node.ResponseSession.name(), id));
    }

    public List<ResponseSessionEntity> findByIds(List<Long> id) {
        return repository.findAllById(id);
    }
}

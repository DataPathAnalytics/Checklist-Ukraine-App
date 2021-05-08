package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.dao.repository.ResponseSessionRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ResponseSessionDaoService {

    private final ResponseSessionRepository repository;

    public SessionEntity save(SessionEntity entity) {
        return repository.save(entity);
    }

    public SessionEntity findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Entity.ResponseSession.name(), id));
    }

    public List<ExportSessionResponseDomain> getResponseSessionDates(LocalDateTime dateModified, int limit) {
        return repository.getResponseSessionDates(dateModified, limit);
    }
}

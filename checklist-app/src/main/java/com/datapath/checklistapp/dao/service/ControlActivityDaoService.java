package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.repository.ControlActivityRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class ControlActivityDaoService {

    private final ControlActivityRepository repository;

    public List<ControlActivityEntity> findAll(Integer userId) {
        if (nonNull(userId)) return repository.findUserControlActivities(userId);
        return repository.findAll();
    }

    public ControlActivityEntity save(ControlActivityEntity entity) {
        return repository.save(entity);
    }

    public ControlActivityEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.ControlActivity.name(), id));
    }

    public List<ExportControlActivityDomain> getControlActivityDates(LocalDateTime offset, int limit) {
        return repository.getControlActivityDates(offset, limit);
    }
}

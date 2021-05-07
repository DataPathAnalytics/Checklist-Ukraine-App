package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.ActivityStatus;
import com.datapath.checklistapp.dao.repository.classifier.ActivityStatusRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityStatusDaoService {

    private final ActivityStatusRepository repository;

    public List<ActivityStatus> findAll() {
        return repository.findAll();
    }

    public ActivityStatus findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.ActivityStatus.name(), id));
    }
}

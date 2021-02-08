package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.ActivityStatus;
import com.datapath.checklistukraineapp.dao.repository.classifier.ActivityStatusRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import com.datapath.checklistukraineapp.util.database.Node;
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.ActivityStatus.name(), id));
    }
}

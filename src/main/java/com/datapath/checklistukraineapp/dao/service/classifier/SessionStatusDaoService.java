package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.SessionStatus;
import com.datapath.checklistukraineapp.dao.repository.classifier.SessionStatusRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SessionStatusDaoService {

    private final SessionStatusRepository repository;

    public List<SessionStatus> findAll() {
        return repository.findAll();
    }

    public SessionStatus findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("sessionStatus", id));
    }
}

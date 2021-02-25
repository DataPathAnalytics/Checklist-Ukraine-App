package com.datapath.checklistapp.dao.service.classifier;

import com.datapath.checklistapp.dao.entity.classifier.SessionStatus;
import com.datapath.checklistapp.dao.repository.classifier.SessionStatusRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Node;
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
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Node.SessionStatus.name(), id));
    }
}

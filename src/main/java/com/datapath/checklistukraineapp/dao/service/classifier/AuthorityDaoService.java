package com.datapath.checklistukraineapp.dao.service.classifier;

import com.datapath.checklistukraineapp.dao.entity.classifier.Authority;
import com.datapath.checklistukraineapp.dao.repository.classifier.AuthorityRepository;
import com.datapath.checklistukraineapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorityDaoService {

    private final AuthorityRepository repository;

    public List<Authority> findAll() {
        return repository.findAll();
    }

    public Authority findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("authority", id));
    }
}

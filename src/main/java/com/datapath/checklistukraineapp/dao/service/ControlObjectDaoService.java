package com.datapath.checklistukraineapp.dao.service;

import com.datapath.checklistukraineapp.dao.repository.ControlObjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ControlObjectDaoService {

    private final ControlObjectRepository repository;

}

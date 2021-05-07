package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateFolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateFolderRepository extends JpaRepository<TemplateFolderEntity, Long> {
    Page<TemplateFolderEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);
}

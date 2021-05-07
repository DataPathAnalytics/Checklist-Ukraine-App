package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateConfigFolderRepository extends JpaRepository<TemplateConfigFolderEntity, Long> {
    Page<TemplateConfigFolderEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);
}

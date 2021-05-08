package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigFolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemplateConfigFolderRepository extends JpaRepository<TemplateConfigFolderEntity, Integer> {

    @Query(value = "select * from template_config_folder where name ilike CONCAT(:name, '%') order by name", nativeQuery = true)
    Page<TemplateConfigFolderEntity> searchByName(@Param("name") String name, Pageable pageable);
}

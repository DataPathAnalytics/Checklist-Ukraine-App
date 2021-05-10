package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateConfigRepository extends JpaRepository<TemplateConfigEntity, Integer> {

    String IS_USED_QUERY = "select exists(select id from session where template_config_id = :id) or " +
            "exists(select id from template where config_id = :id)";

    List<TemplateConfigEntity> findAllByType(TemplateConfigType type);

    @Query(value = "select * from template_config where name ilike CONCAT(:name, '%') order by name", nativeQuery = true)
    Page<TemplateConfigEntity> searchByName(@Param("name") String name, Pageable pageable);

    @Query(value = IS_USED_QUERY, nativeQuery = true)
    boolean isUsed(Integer id);
}

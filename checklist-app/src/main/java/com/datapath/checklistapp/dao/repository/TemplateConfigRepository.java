package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.dao.entity.classifier.TemplateConfigType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TemplateConfigRepository extends JpaRepository<TemplateConfigEntity, Long> {

    String IS_USED_QUERY = "select exists(select id from response_session where template_config_id = :id) or " +
            "exists(select id from template where config_id = :id)";

    List<TemplateConfigEntity> findAllByType(TemplateConfigType type);

    Page<TemplateConfigEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    @Query(value = IS_USED_QUERY, nativeQuery = true)
    boolean isUsed(Long id);
}

package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Long> {

    String IS_USED_QUERY = "select exists(select id from response_session where template_id = :id)";

    Page<TemplateEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);

    @Query(value = IS_USED_QUERY, nativeQuery = true)
    boolean isUsed(@Param("id") Long id);
}

package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {

    String IS_USED_QUERY = "select exists(select id from session where template_id = :id)";

    @Query(value = "select * from template where name ilike CONCAT(:name, '%') order by name", nativeQuery = true)
    Page<TemplateEntity> searchByName(@Param("name") String name, Pageable pageable);

    @Query(value = IS_USED_QUERY, nativeQuery = true)
    boolean isUsed(@Param("id") Integer id);
}

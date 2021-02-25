package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    @Query(value = "match (q:Question)-->(qt:QuestionType)<--(tt:TemplateType) where tt.templateTypeId = $templateTypeId return id(q)")
    List<Long> getByTemplateType(Integer templateTypeId);

    @Query(value = "match (q:Question)-->(qt:QuestionType) where qt.questionTypeId = $questionTypeId return id(q)")
    List<Long> getByQuestionType(Integer questionTypeId);

    Page<QuestionEntity> findByNameMatchesRegexOrderByName(String name, Pageable pageable);
}

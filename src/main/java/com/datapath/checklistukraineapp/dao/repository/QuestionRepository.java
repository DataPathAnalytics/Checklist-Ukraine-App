package com.datapath.checklistukraineapp.dao.repository;

import com.datapath.checklistukraineapp.dao.domain.QuestionDomain;
import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface QuestionRepository extends Neo4jRepository<QuestionEntity, Long> {

    @Query(value = "match (t:Template)-[r:HAS_QUESTION]->(q:Question)-[f:FROM_QUESTION_SOURCE]->(qs:QuestionSource)\n" +
            "where id(t) = $templateId\n" +
            "return q,\n" +
            "qs.id as questionSourceId,\n" +
            "qs.name as questionSourceName,\n" +
            "qs.link as questionSourceLink,\n" +
            "f.documentParagraph as documentParagraph,\n" +
            "r.groupName as groupName,\n" +
            "r.parentQuestionId as parentQuestionId,\n" +
            "r.answerId as parentAnswerId")
    List<QuestionDomain> findQuestions(Long templateId);
}

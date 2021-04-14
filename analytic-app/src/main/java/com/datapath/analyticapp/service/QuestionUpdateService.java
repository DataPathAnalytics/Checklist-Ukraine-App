package com.datapath.analyticapp.service;

import com.datapath.analyticapp.dao.entity.AnswerStructureEntity;
import com.datapath.analyticapp.dao.entity.QuestionEntity;
import com.datapath.analyticapp.dao.repository.AnswerStructureRepository;
import com.datapath.analyticapp.dao.repository.KnowledgeClassRepository;
import com.datapath.analyticapp.dao.repository.QuestionRepository;
import com.datapath.analyticapp.dto.export.question.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionUpdateService {

    private final QuestionRepository questionRepository;
    private final KnowledgeClassRepository knowledgeClassRepository;
    private final AnswerStructureRepository answerStructureRepository;

    @Transactional
    public void process(QuestionDTO questionDto) {
        QuestionEntity question = new QuestionEntity();
        question.setOuterId(questionDto.getId());
        question.setValue(questionDto.getValue());
        question.setDateCreated(questionDto.getDateCreated());
        question.setKnowledgeClasses(knowledgeClassRepository.findAllById(questionDto.getKnowledgeClasses()));

        AnswerStructureEntity answerStructureEntity = answerStructureRepository.findFirstByOuterId(questionDto.getAnswerStructure().getId());

        if (isNull(answerStructureEntity)) {
            answerStructureEntity = new AnswerStructureEntity();
            answerStructureEntity.setOuterId(questionDto.getAnswerStructure().getId());
            answerStructureEntity.setName(questionDto.getAnswerStructure().getName());
            answerStructureEntity.setIdentifierFieldName(
                    questionDto.getAnswerStructure().getFieldDescriptions().stream()
                            .filter(QuestionDTO.FieldDescriptionDTO::isIdentifier)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Identifier not found. AnswerStructure id " + questionDto.getAnswerStructure().getId()))
                            .getName()
            );
        }

        question.setAnswerStructure(answerStructureEntity);

        questionRepository.save(question);
    }
}

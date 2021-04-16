package com.datapath.analyticapp.service.imported.question;

import com.datapath.analyticapp.dao.entity.AnswerStructureEntity;
import com.datapath.analyticapp.dao.entity.FieldDescriptionEntity;
import com.datapath.analyticapp.dao.entity.QuestionEntity;
import com.datapath.analyticapp.dao.repository.AnswerStructureRepository;
import com.datapath.analyticapp.dao.repository.KnowledgeClassRepository;
import com.datapath.analyticapp.dao.repository.QuestionRepository;
import com.datapath.analyticapp.dto.imported.question.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

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
                            .orElse(new QuestionDTO.FieldDescriptionDTO())
                            .getName()
            );

            answerStructureEntity.setFields(questionDto.getAnswerStructure().getFieldDescriptions().stream()
                    .map(dto -> {
                        FieldDescriptionEntity entity = new FieldDescriptionEntity();
                        entity.setOuterId(dto.getId());
                        entity.setName(dto.getName());
                        entity.setType(dto.getValueType());

                        if (!isEmpty(dto.getValues())) {
                            Map<String, String> values = dto.getValues().stream()
                                    .collect(toMap(q -> q.getId().toString(), QuestionDTO.ValueDTO::getValue));

                            entity.setValues(values);
                        }

                        return entity;
                    }).collect(toList()));
        }

        question.setAnswerStructure(answerStructureEntity);

        questionRepository.save(question);
    }
}

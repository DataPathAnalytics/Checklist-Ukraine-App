package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.request.question.CreateQuestionRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.page.PageableResponse;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class QuestionWebService {

    private final QuestionDaoService service;
    private final AnswerStructureDaoService answerStructureService;
    private final QuestionConverter questionConverter;
    private final QuestionSourceDaoService questionSourceService;

    @Transactional
    public void create(CreateQuestionRequest request) {
        QuestionEntity entity = new QuestionEntity();

        entity.setValue(request.getValue());

        if (!isEmpty(request.getKnowledgeClassIds())) {
            entity.setKnowledgeClasses(new HashSet<>(request.getKnowledgeClassIds()));
        }

        if (nonNull(request.getSource())) {
            QuestionSourceEntity source = questionSourceService.findByIdentifier(request.getSource().getIdentifier());

            if (isNull(source)) {
                source = new QuestionSourceEntity();
                source.setIdentifier(request.getSource().getIdentifier());
            }

            source.setName(request.getSource().getName());
            source.setLink(request.getSource().getLink());

            entity.setQuestionSource(source);
        }

        if (nonNull(request.getAnswerStructureId())) {
            entity.setAnswerStructure(answerStructureService.findById(request.getAnswerStructureId()));
        } else {
            entity.setAnswerStructure(answerStructureService.findDefault());
        }

        service.save(entity);
    }

    public QuestionDTO get(Integer id) {
        return questionConverter.map(service.findById(id));
    }

    @Transactional
    public PageableResponse<QuestionDTO> search(SearchRequest request) {
        Page<QuestionEntity> page = service.searchByValue(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(questionConverter::map)
                        .collect(toList())
        );
    }

    @Transactional
    public PageableResponse<QuestionDTO> searchWithIdentifier(SearchRequest request) {
        Page<QuestionEntity> page = service.searchWithIdentifierByValue(request);

        return new PageableResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(questionConverter::map)
                        .collect(toList())
        );
    }
}

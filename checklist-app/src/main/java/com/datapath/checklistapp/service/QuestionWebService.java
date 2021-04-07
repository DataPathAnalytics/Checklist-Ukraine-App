package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.KnowledgeClassEntity;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
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

import java.util.Comparator;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class QuestionWebService {

    private final QuestionDaoService service;
    private final QuestionSourceDaoService sourceService;
    private final AnswerStructureDaoService answerStructureService;
    private final QuestionConverter questionConverter;

    @Transactional
    public void create(CreateQuestionRequest request) {
        QuestionEntity entity = new QuestionEntity();

        entity.setValue(request.getValue());
        entity.getKnowledgeClasses().addAll(
                request.getKnowledgeCategoryIds().stream()
                        .map(KnowledgeClassEntity::new)
                        .collect(toSet())
        );

        if (nonNull(request.getQuestionSourceName())) {
            entity.setQuestionSourceName(request.getQuestionSourceName());
            entity.setQuestionSourceLink(request.getQuestionSourceLink());
        }

        if (nonNull(request.getAnswerStructureId())) {
            entity.setAnswerStructure(answerStructureService.findById(request.getAnswerStructureId()));
        } else {
            entity.setAnswerStructure(answerStructureService.findDefault());
        }

        service.save(entity);
    }

    public QuestionDTO get(Long id) {
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
        List<Long> ids = service.searchWithIdentifierByValue(request);
        Long count = service.countWithIdentifierByValue(request);

        return new PageableResponse<>(
                request.getPage(),
                count,
                (int) Math.ceil((double) count / request.getSize()),
                service.findById(ids)
                        .stream()
                        .map(questionConverter::map)
                        .sorted(Comparator.comparing(QuestionDTO::getValue))
                        .collect(toList())
        );
    }
}

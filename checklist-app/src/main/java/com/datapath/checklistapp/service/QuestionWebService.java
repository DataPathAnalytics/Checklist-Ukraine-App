package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.KnowledgeCategoryEntity;
import com.datapath.checklistapp.dao.entity.LinkTypeEntity;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistapp.dao.relatioship.QuestionSourceRelationship;
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

        entity.setName(request.getName());
        entity.getKnowledgeCategory().addAll(
                request.getKnowledgeCategoryIds().stream()
                        .map(KnowledgeCategoryEntity::new)
                        .collect(toSet())
        );

        entity.setLinkTypes(request.getLinkTypes()
                .stream()
                .map(LinkTypeEntity::new)
                .collect(toSet())
        );

        if (nonNull(request.getQuestionSourceId())) {
            QuestionSourceEntity questionSource = sourceService.findById(request.getQuestionSourceId());
            QuestionSourceRelationship relationship = new QuestionSourceRelationship();
            relationship.setSource(questionSource);
            relationship.setDocumentParagraph(request.getQuestionSourceParagraph());
            entity.setSource(relationship);
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
        Page<QuestionEntity> page = service.searchByName(request);

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
        Page<Long> idsPage = service.searchWithIdentifierByName(request);

        return new PageableResponse<>(
                idsPage.getNumber(),
                idsPage.getTotalElements(),
                idsPage.getTotalPages(),
                service.findById(idsPage.getContent()).stream()
                        .map(questionConverter::map)
                        .collect(toList())
        );
    }
}

package com.datapath.checklistapp.service;

import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistapp.dao.relatioship.QuestionSourceRelationship;
import com.datapath.checklistapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistapp.dao.service.KnowledgeCategoryDaoService;
import com.datapath.checklistapp.dao.service.QuestionDaoService;
import com.datapath.checklistapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistapp.dao.service.classifier.QuestionTypeDaoService;
import com.datapath.checklistapp.dto.QuestionDTO;
import com.datapath.checklistapp.dto.QuestionTypeDTO;
import com.datapath.checklistapp.dto.request.question.CreateQuestionRequest;
import com.datapath.checklistapp.dto.request.search.SearchRequest;
import com.datapath.checklistapp.dto.response.search.SearchResponse;
import com.datapath.checklistapp.service.converter.structure.QuestionConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.datapath.checklistapp.util.Constants.FACT_QUESTION_TYPE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class QuestionWebService {

    private final QuestionDaoService service;
    private final QuestionSourceDaoService sourceService;
    private final KnowledgeCategoryDaoService knowledgeCategoryService;
    private final AnswerStructureDaoService answerStructureService;
    private final QuestionTypeDaoService questionTypeService;
    private final QuestionConverter questionConverter;

    @Transactional
    public void create(CreateQuestionRequest request) {
        QuestionEntity entity = new QuestionEntity();

        entity.setName(request.getName());
        entity.getKnowledgeCategory().addAll(knowledgeCategoryService.findByIds(request.getKnowledgeCategoryIds()));
        entity.setType(questionTypeService.findById(request.getQuestionTypeId()));

        if (nonNull(request.getQuestionSourceId())) {
            QuestionSourceEntity questionSource = sourceService.findById(request.getQuestionSourceId());
            QuestionSourceRelationship relationship = new QuestionSourceRelationship();
            relationship.setSource(questionSource);
            relationship.setDocumentParagraph(request.getQuestionSourceParagraph());
            entity.setSource(relationship);
        }

        if (!FACT_QUESTION_TYPE.equals(request.getQuestionTypeId())) {
            entity.setAnswerStructure(answerStructureService.findById(request.getAnswerStructureId()));
        }

        service.save(entity);
    }

    public QuestionDTO get(Long id) {
        return questionConverter.map(service.findById(id));
    }

    public List<QuestionTypeDTO> listByTemplateType(Integer templateTypeId) {
        List<Long> ids = service.findByTemplateType(templateTypeId);

        Map<Integer, List<QuestionDTO>> questionsByType = service.findById(ids)
                .stream()
                .map(questionConverter::map)
                .collect(groupingBy(QuestionDTO::getQuestionTypeId));
        return questionsByType.entrySet()
                .stream()
                .map(e -> new QuestionTypeDTO(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public List<QuestionTypeDTO> listByQuestionType(Integer questionTypeId) {
        List<Long> ids = service.findByQuestionType(questionTypeId);

        Map<Integer, List<QuestionDTO>> questionsByType = service.findById(ids)
                .stream()
                .map(questionConverter::map)
                .collect(groupingBy(QuestionDTO::getQuestionTypeId));
        return questionsByType.entrySet()
                .stream()
                .map(e -> new QuestionTypeDTO(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public List<QuestionDTO> list() {
        return service.findAll()
                .stream()
                .map(questionConverter::map)
                .collect(toList());
    }

    public SearchResponse<QuestionDTO> search(SearchRequest request) {
        Page<QuestionEntity> page = service.searchByName(request);

        return new SearchResponse<>(
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.get()
                        .map(questionConverter::map)
                        .collect(toList())
        );
    }
}

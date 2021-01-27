package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistukraineapp.dao.relatioship.QuestionSourceRelationship;
import com.datapath.checklistukraineapp.dao.service.AnswerStructureDaoService;
import com.datapath.checklistukraineapp.dao.service.QuestionDaoService;
import com.datapath.checklistukraineapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.KnowledgeCategoryDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.QuestionTypeDaoService;
import com.datapath.checklistukraineapp.dao.service.classifier.TemplateTypeDaoService;
import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.QuestionTypeDTO;
import com.datapath.checklistukraineapp.dto.request.CreateQuestionRequest;
import com.datapath.checklistukraineapp.util.DtoEntityConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.datapath.checklistukraineapp.util.Constants.FACT_QUESTION_TYPE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class QuestionWebService {

    private final QuestionDaoService service;
    private final QuestionSourceDaoService sourceService;
    private final KnowledgeCategoryDaoService knowledgeCategoryService;
    private final AnswerStructureDaoService answerStructureService;
    private final TemplateTypeDaoService templateTypeService;
    private final QuestionTypeDaoService questionTypeService;

    @Transactional
    public void create(CreateQuestionRequest request) {
        QuestionEntity entity = new QuestionEntity();

        entity.setName(request.getName());
        entity.setKnowledgeCategory(knowledgeCategoryService.findById(request.getKnowledgeCategoryId()));
        entity.setType(questionTypeService.findById(request.getQuestionTypeId()));

        QuestionSourceEntity questionSource = sourceService.findById(request.getQuestionSourceId());
        QuestionSourceRelationship relationship = new QuestionSourceRelationship();
        relationship.setSource(questionSource);
        relationship.setDocumentParagraph(request.getQuestionSourceParagraph());
        entity.setSource(relationship);

        if (!FACT_QUESTION_TYPE.equals(request.getQuestionTypeId())) {
            entity.setAnswerStructure(answerStructureService.findById(request.getAnswerStructureId()));
        }

        service.save(entity);
    }

    public QuestionDTO get(Long id) {
        return DtoEntityConverter.map(service.findById(id));
    }

    public List<QuestionTypeDTO> listByTemplateType(Integer templateTypeId) {
        Map<Integer, List<QuestionDTO>> questionsByType = service.findByTypes(templateTypeService.findById(templateTypeId).getQuestionTypes())
                .stream()
                .map(DtoEntityConverter::map)
                .collect(groupingBy(QuestionDTO::getQuestionTypeId));
        return questionsByType.entrySet()
                .stream()
                .map(e -> new QuestionTypeDTO(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public List<QuestionTypeDTO> listByQuestionType(Integer questionTypeId) {
        Map<Integer, List<QuestionDTO>> questionsByType = service.findByTypes(
                Collections.singletonList(questionTypeService.findById(questionTypeId))
        )
                .stream()
                .map(DtoEntityConverter::map)
                .collect(groupingBy(QuestionDTO::getQuestionTypeId));
        return questionsByType.entrySet()
                .stream()
                .map(e -> new QuestionTypeDTO(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public List<QuestionDTO> list() {
        return service.findAll()
                .stream()
                .map(DtoEntityConverter::map)
                .collect(toList());
    }
}

package com.datapath.checklistukraineapp.service;

import com.datapath.checklistukraineapp.dao.entity.QuestionEntity;
import com.datapath.checklistukraineapp.dao.entity.QuestionSourceEntity;
import com.datapath.checklistukraineapp.dao.relatioship.QuestionSourceRelationship;
import com.datapath.checklistukraineapp.dao.service.QuestionDaoService;
import com.datapath.checklistukraineapp.dao.service.QuestionSourceDaoService;
import com.datapath.checklistukraineapp.dto.QuestionDTO;
import com.datapath.checklistukraineapp.dto.request.CreateQuestionRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class QuestionWebService {

    private final QuestionDaoService service;
    private final QuestionSourceDaoService sourceService;

    @Transactional
    public void create(CreateQuestionRequest request) {
        QuestionEntity entity = new QuestionEntity();
        entity.setName(request.getName());

        QuestionSourceEntity questionSource = sourceService.findById(request.getQuestionSourceId());
        QuestionSourceRelationship relationship = new QuestionSourceRelationship();
        relationship.setSource(questionSource);
        relationship.setDocumentParagraph(request.getQuestionSourceParagraph());

        entity.setSource(relationship);

        service.save(entity);
    }

    public List<QuestionDTO> list() {
        return service.findAll().stream()
                .map(q -> {
                    QuestionDTO dto = new QuestionDTO();

                    BeanUtils.copyProperties(q, dto);

                    dto.setQuestionSourceId(q.getSource().getSource().getId());
                    dto.setDocumentParagraph(q.getSource().getDocumentParagraph());

                    return dto;
                })
                .collect(toList());
    }

    public QuestionDTO get(Long id) {
        QuestionEntity entity = service.findById(id);

        QuestionDTO dto = new QuestionDTO();

        BeanUtils.copyProperties(entity, dto);

        dto.setQuestionSourceId(entity.getSource().getSource().getId());
        dto.setDocumentParagraph(entity.getSource().getDocumentParagraph());

        return dto;
    }
}

package com.datapath.checklistapp.service.validation;

import com.datapath.checklistapp.dao.entity.FieldDescriptionEntity;
import com.datapath.checklistapp.dao.entity.QuestionEntity;
import com.datapath.checklistapp.dao.entity.TemplateConfigEntity;
import com.datapath.checklistapp.exception.ValidationException;
import org.springframework.stereotype.Service;

import static com.datapath.checklistapp.util.Constants.SESSION_TEMPLATE_TYPE;

@Service
public class ValidateService {

    public void validate(QuestionEntity question) {
        if (noIdentifier(question))
            throw new ValidationException("Answer structure must has identifier fields");
    }

    public void validate(TemplateConfigEntity config) {
        if (!SESSION_TEMPLATE_TYPE.equals(config.getType().getId()))
            throw new ValidationException("Invalid template config type. Should by response session template config type.");
    }

    private boolean noIdentifier(QuestionEntity question) {
        return question.getAnswerStructure().getFields()
                .stream()
                .noneMatch(FieldDescriptionEntity::isIdentifier);
    }
}

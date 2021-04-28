package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
import com.datapath.analyticapp.dao.service.request.DeleteRequest;
import com.datapath.analyticapp.dao.service.request.EventRequest;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.exception.ValidationException;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.datapath.analyticapp.Constants.*;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Slf4j
public class SessionUpdateService extends BaseUpdateService {

    private final ResponseSessionRepository responseSessionRepository;
    private final ControlActivityRepository controlActivityRepository;

    public SessionUpdateService(CypherQueryService dbUtils,
                                MinerRuleProvider minerRuleProvider,
                                RoleRepository roleRepository,
                                NodeTypeRepository nodeTypeRepository,
                                UserRepository userRepository,
                                ResponseSessionRepository responseSessionRepository,
                                QuestionRepository questionRepository,
                                KnowledgeClassRepository knowledgeClassRepository,
                                ControlActivityRepository controlActivityRepository) {
        super(dbUtils, minerRuleProvider, roleRepository, nodeTypeRepository, userRepository, questionRepository, knowledgeClassRepository);
        this.responseSessionRepository = responseSessionRepository;
        this.controlActivityRepository = controlActivityRepository;
    }

    @Transactional
    public void update(ResponseDataDTO response) {
        log.info("Updating response session in control activity {}", response.getId());

        ControlActivityEntity activity = controlActivityRepository.findByOuterId(response.getId());

        response.getSessions().forEach(session -> {
            log.info("Updating response session {}", session.getId());

            roleNodeIdMap = new HashMap<>();
            addNewRoleNodeId(CONTROL_ACTIVITY_ROLE, activity.getId());

            handleResponseSession(session);
            handleSubject(session);
            handleTypeQuestions(session, RESPONSE_SESSION_ROLE);
            handleRuleMining();
        });
    }

    private void handleResponseSession(SessionDTO session) {
        ResponseSessionEntity sessionEntity = responseSessionRepository.findByOuterId(session.getId());
        if (isNull(sessionEntity)) {
            sessionEntity = new ResponseSessionEntity();
            sessionEntity.setOuterId(session.getId());
            sessionEntity.setDateCreated(session.getDateCreated());
            setAuthor(sessionEntity, session.getAuthorId());
        }
        setReviewer(sessionEntity, session.getReviewerId());
//        sessionEntity.setDateModified(sessionEntity.getDateModified());

        addNewRoleNodeId(RESPONSE_SESSION_ROLE, responseSessionRepository.save(sessionEntity).getId());
    }

    private void handleSubject(SessionDTO session) {
        Map<Long, AnswerDTO> questionAnswers = session.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = session.getTemplate().getObjectFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        questions.putAll(session.getTemplate().getUngroupedQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity())));

        questions.putAll(session.getTemplate().getQuestionGroups().stream()
                .flatMap(group -> group.getQuestions().stream())
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity())));

        QuestionExecutionDTO subjectQuestion = session.getTemplate().getObjectQuestion();
        Map<String, Object> answerValue = questionAnswers.get(subjectQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        FieldDescriptionDTO identifier = getIdentifierField(subjectQuestion)
                .orElseThrow(() -> new ValidationException("Subject must have identifier field"));

        String nodeType = getNodeType(subjectQuestion, SUBJECT_DEFAULT_NODE);

        Long nodeId = queryService.mergeIdentifierNode(
                QueryRequestBuilder.identifierRequest(
                        nodeType,
                        identifier.getName(),
                        answerValue.get(identifier.getName()),
                        identifier.getValueType(),
                        answerValue)
        );
        addNewRoleNodeId(SUBJECT_ROLE, nodeId);

        handleSubQuestions(subjectQuestion, nodeId, questions, questionAnswers);
    }

    protected void handleQuestion(QuestionExecutionDTO execution,
                                  Long parentId,
                                  Map<Long, QuestionExecutionDTO> questions,
                                  Map<Long, AnswerDTO> questionAnswers) {
        AnswerDTO answer = questionAnswers.get(execution.getId());
        if (isNull(answer)) return;
        Map<String, Object> answerValue = answer.getValues();
        if (isEmpty(answerValue)) return;

        if (isFact(execution)) {
            handleFactQuestion(execution, parentId, questions, questionAnswers);
        } else {
            super.handleQuestion(execution, parentId, questions, questionAnswers);
        }
    }

    private void handleFactQuestion(QuestionExecutionDTO execution,
                                    Long parentId,
                                    Map<Long, QuestionExecutionDTO> questions,
                                    Map<Long, AnswerDTO> questionAnswers) {
        Long questionId = getUpdatedQuestionId(execution.getQuestion());

        FieldDescriptionDTO fieldDescription = execution.getQuestion().getAnswerStructure().getFieldDescriptions().get(0);
        String fieldName = fieldDescription.getName();
        AnswerDTO answer = questionAnswers.get(execution.getId());
        Long answerValueId = Long.parseLong(answer.getValues().get(fieldName).toString());
        String answerValue = execution.getQuestion().getAnswerStructure().getFieldDescriptions().get(0).getValues()
                .stream()
                .filter(v -> v.getId().equals(answerValueId))
                .findFirst()
                .map(FieldDescriptionDTO.ValueDTO::getValue).orElseThrow(() -> new ValidationException("Not found value for fact answer"));

        Long nodeId = queryService.mergeFactNode(
                QueryRequestBuilder.factRequest(parentId, answerValue, fieldName, fieldDescription.getValueType(), execution.getQuestion().getValue())
        );

        addNewRoleNodeId(FACT_ROLE, nodeId);
        addNewRoleNodeId(execution, nodeId);
        handleQuestionRelationship(nodeId, questionId);
        handleEventProcessing(nodeId, answerValueId, execution.getConditionCharacteristics());
        handleSubQuestions(execution, answerValueId, nodeId, questions, questionAnswers);
    }

    private void handleQuestionRelationship(Long nodeId, Long questionId) {
        queryService.buildRelationship(QueryRequestBuilder.relationshipRequest(nodeId, questionId, FACT_QUESTION_LINK));
        queryService.buildRelationship(QueryRequestBuilder.relationshipRequest(roleNodeIdMap.get(RESPONSE_SESSION_ROLE).get(0), questionId, SESSION_QUESTION_LINK));
        queryService.buildRelationship(QueryRequestBuilder.relationshipRequest(roleNodeIdMap.get(SUBJECT_ROLE).get(0), questionId, SUBJECT_QUESTION_LINK));
    }

    private void handleEventProcessing(Long factNodeId, Long answerValueId, List<ConditionCharacteristicDTO> conditionCharacteristics) {
        DeleteRequest request = QueryRequestBuilder.deleteRequest(factNodeId, EVENT_DEFAULT_NODE, EVENT_DEFAULT_LINK);
        queryService.delete(request);
        if (!isEmpty(conditionCharacteristics)) {
            conditionCharacteristics
                    .stream()
                    .filter(cc -> cc.getConditionAnswerId().equals(answerValueId))
                    .findFirst()
                    .ifPresent(cc -> {
                        EventRequest eventRequest = QueryRequestBuilder.eventRequest(factNodeId, cc.getOuterRiskEventId());
                        Long eventId = queryService.createEvent(eventRequest);
                        addNewRoleNodeId(EVENT_ROLE, eventId);
                    });
        }
    }

    private void handleSubQuestions(QuestionExecutionDTO execution,
                                    Long answerValueId,
                                    Long parentId,
                                    Map<Long, QuestionExecutionDTO> questions,
                                    Map<Long, AnswerDTO> questionAnswers) {
        if (!isEmpty(execution.getSubQuestions())) {
            execution.getSubQuestions().forEach(sub -> {
                List<Long> subQuestionIds = sub.getConditionAnswerQuestionIds().get(answerValueId);
                if (!isEmpty(subQuestionIds)) {
                    subQuestionIds
                            .forEach(subQ -> handleQuestion(questions.get(subQ), parentId, questions, questionAnswers));
                }
            });
        }
    }

    private void setAuthor(ResponseSessionEntity session, Long authorId) {
        session.setAuthor(userRepository.findByOuterId(authorId));
    }

    private void setReviewer(ResponseSessionEntity session, Long reviewerId) {
        session.setReviewer(userRepository.findByOuterId(reviewerId));
    }

    private boolean isFact(QuestionExecutionDTO execution) {
        List<FieldDescriptionDTO> fields = execution.getQuestion().getAnswerStructure().getFieldDescriptions();
        return fields.size() == 1 && !isEmpty(fields.get(0).getValues());
    }
}

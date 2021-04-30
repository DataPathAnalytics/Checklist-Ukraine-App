package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
import com.datapath.analyticapp.dao.service.request.EventRequest;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.exception.ValidationException;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import com.datapath.analyticapp.service.miner.config.MinerRulePlace;
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
                                ControlActivityRepository controlActivityRepository,
                                QueryRequestBuilder queryRequestBuilder) {
        super(dbUtils,
                minerRuleProvider,
                roleRepository,
                nodeTypeRepository,
                userRepository,
                questionRepository,
                knowledgeClassRepository,
                queryRequestBuilder);
        this.responseSessionRepository = responseSessionRepository;
        this.controlActivityRepository = controlActivityRepository;
    }

    @Transactional
    public void update(ResponseDataDTO response) {
        log.info("Updating response sessions in control activity {}", response.getId());

        ControlActivityEntity activity = controlActivityRepository.findByOuterId(response.getId());

        response.getSessions().forEach(session -> handle(session, activity));
    }

    private void handle(SessionDTO session, ControlActivityEntity activity) {
        log.info("Updating response session {}", session.getId());

//      deleteSessionCurrentState(session.getId());

        if (session.isInvalid()) return;

        roleNodeIdMap = new HashMap<>();
        addRoleNodeId(CONTROL_ACTIVITY_ROLE, activity.getId());

        handleResponseSession(session);
        handleTypeQuestions(session, RESPONSE_SESSION_ROLE);
        handleSubject(session);
        handleRuleMining(MinerRulePlace.response_session);
    }

    private void handleResponseSession(SessionDTO session) {
        //TODO: remove this logic after delete session logic implemented
        ResponseSessionEntity sessionEntity = responseSessionRepository.findByOuterId(session.getId());
        if (isNull(sessionEntity)) {
            sessionEntity = new ResponseSessionEntity();
            sessionEntity.setOuterId(session.getId());
            sessionEntity.setDateCreated(session.getDateCreated());
            setAuthor(sessionEntity, session.getAuthorId());
        }
        setReviewer(sessionEntity, session.getReviewerId());
        //TODO: remove this logic after logic implemented
//        sessionEntity.setDateModified(sessionEntity.getDateModified());

        addRoleNodeId(RESPONSE_SESSION_ROLE, responseSessionRepository.save(sessionEntity).getId());
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
                queryRequestBuilder.identifierRequest(
                        nodeType,
                        identifier.getName(),
                        answerValue.get(identifier.getName()),
                        answerValue,
                        getFieldTypes(subjectQuestion))
        );
        addRoleNodeId(SUBJECT_ROLE, nodeId);

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

        Map<String, Object> props = new HashMap<>();
        props.put(fieldName, answerValue);

        Long nodeId = queryService.mergeFactNode(
                queryRequestBuilder.factRequest(parentId, fieldName, execution.getQuestion().getValue(), props, getFieldTypes(execution))
        );

        addRoleNodeId(FACT_ROLE, nodeId);
        addRoleNodeId(execution, nodeId);
        handleQuestionRelationship(nodeId, parentId, questionId);
        handleEventProcessing(nodeId, answerValueId, execution.getConditionCharacteristics());
        handleSubQuestions(execution, answerValueId, nodeId, questions, questionAnswers);
    }

    private void handleQuestionRelationship(Long nodeId, Long parentId, Long questionId) {
        queryService.buildRelationship(queryRequestBuilder.relationshipRequest(nodeId, questionId, FACT_QUESTION_LINK));
        queryService.buildRelationship(queryRequestBuilder.relationshipRequest(roleNodeIdMap.get(RESPONSE_SESSION_ROLE).get(0), questionId, SESSION_QUESTION_LINK));
        queryService.buildRelationship(queryRequestBuilder.relationshipRequest(parentId, questionId, PARENT_QUESTION_LINK));
    }

    private void handleEventProcessing(Long factNodeId, Long answerValueId, List<ConditionCharacteristicDTO> conditionCharacteristics) {
        if (!isEmpty(conditionCharacteristics)) {
            conditionCharacteristics
                    .stream()
                    .filter(cc -> cc.getConditionAnswerId().equals(answerValueId))
                    .findFirst()
                    .ifPresent(cc -> {
                        EventRequest eventRequest = queryRequestBuilder.eventRequest(factNodeId, cc.getOuterRiskEventId());
                        Long eventId = queryService.createEvent(eventRequest);
                        addRoleNodeId(EVENT_ROLE, eventId);
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

package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequest;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.exception.ValidationException;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    private final static String ACTIVITY_SESSION_LINK = "HAS_RESPONSE_SESSION";

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

            Map<String, List<Long>> roleNodeId = new HashMap<>();
            roleNodeId.put(CONTROL_ACTIVITY_ROLE, Collections.singletonList(activity.getId()));

            handleResponseSession(session, roleNodeId);
            handleSubject(session, roleNodeId);
            handleTypeQuestions(session, roleNodeId, RESPONSE_SESSION_ROLE);
            handleQuestions(session, roleNodeId);
        });
    }

    private void handleResponseSession(SessionDTO session, Map<String, List<Long>> roleNodeId) {
        ResponseSessionEntity sessionEntity = responseSessionRepository.findByOuterId(session.getId());
        if (isNull(sessionEntity)) {
            sessionEntity = new ResponseSessionEntity();
            sessionEntity.setOuterId(session.getId());
            sessionEntity.setDateCreated(session.getDateCreated());
            setAuthor(sessionEntity, session.getAuthorId());
        }
        setReviewer(sessionEntity, session.getReviewerId());
//        sessionEntity.setDateModified(sessionEntity.getDateModified());

        Long savedId = responseSessionRepository.save(sessionEntity).getId();
        roleNodeId.put(RESPONSE_SESSION_ROLE, Collections.singletonList(savedId));

        queryService.buildRelationship(
                QueryRequest.forRelationship(roleNodeId.get(CONTROL_ACTIVITY_ROLE).get(0), savedId, ACTIVITY_SESSION_LINK)
        );
    }

    private void handleSubject(SessionDTO session, Map<String, List<Long>> roleNodeId) {
        Map<Long, AnswerDTO> questionAnswers = session.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = session.getTemplate().getObjectFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        QuestionExecutionDTO subjectQuestion = session.getTemplate().getObjectQuestion();
        Map<String, Object> answerValue = questionAnswers.get(subjectQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        FieldDescriptionDTO identifier = getIdentifierField(subjectQuestion)
                .orElseThrow(() -> new ValidationException("Subject must have identifier field"));

        String nodeType = getNodeType(subjectQuestion, SUBJECT_DEFAULT_NODE);

        Long nodeId = queryService.mergeIdentifierNode(
                QueryRequest.forIdentifier(
                        nodeType,
                        identifier.getName(),
                        answerValue.get(identifier.getName()),
                        identifier.getValueType(),
                        answerValue)
        );

        handleRuleProcessing(nodeId, SUBJECT_ROLE, roleNodeId);
        super.handleSubQuestions(subjectQuestion, nodeId, questions, questionAnswers, roleNodeId);
    }

    private void handleQuestions(SessionDTO session, Map<String, List<Long>> roleNodeId) {
        Map<Long, AnswerDTO> questionAnswers = session.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = session.getTemplate().getUngroupedQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        session.getTemplate().getUngroupedQuestions()
                .stream()
                .filter(QuestionExecutionDTO::isRoot)
                .forEach(execution -> handleQuestion(execution, roleNodeId.get(RESPONSE_SESSION_ROLE).get(0), questions, questionAnswers, roleNodeId));

        session.getTemplate().getQuestionGroups()
                .forEach(qg -> qg.getQuestions().stream()
                        .filter(QuestionExecutionDTO::isRoot)
                        .forEach(execution -> handleQuestion(execution, roleNodeId.get(RESPONSE_SESSION_ROLE).get(0), questions, questionAnswers, roleNodeId)));
    }

    protected void handleQuestion(QuestionExecutionDTO execution,
                                  Long parentId,
                                  Map<Long, QuestionExecutionDTO> questions,
                                  Map<Long, AnswerDTO> questionAnswers,
                                  Map<String, List<Long>> roleNodeId) {
        AnswerDTO answer = questionAnswers.get(execution.getId());
        if (isNull(answer)) return;
        Map<String, Object> answerValue = answer.getValues();
        if (isEmpty(answerValue)) return;

        if (isFact(execution)) {
            handleFactQuestion(execution, parentId, questions, questionAnswers, roleNodeId);
        } else {
            super.handleQuestion(execution, parentId, questions, questionAnswers, roleNodeId);
        }
    }

    private void handleFactQuestion(QuestionExecutionDTO execution,
                                    Long parentId,
                                    Map<Long, QuestionExecutionDTO> questions,
                                    Map<Long, AnswerDTO> questionAnswers,
                                    Map<String, List<Long>> roleNodeId) {
        Long questionId = getUpdatedQuestionId(execution.getQuestion());

        String fieldName = execution.getQuestion().getAnswerStructure().getFieldDescriptions().get(0).getName();
        AnswerDTO answer = questionAnswers.get(execution.getId());
        Long answerValueId = Long.parseLong(answer.getValues().get(fieldName).toString());
        String answerValue = execution.getQuestion().getAnswerStructure().getFieldDescriptions().get(0).getValues()
                .stream()
                .filter(v -> v.getId().equals(answerValueId))
                .findFirst()
                .map(FieldDescriptionDTO.ValueDTO::getValue).orElseThrow(() -> new ValidationException("Not found value for fact answer"));

        Map<String, Object> props = new HashMap<>();
        props.put(fieldName, answerValue);
        props.put("questionValue", execution.getQuestion().getValue());

        Long nodeId = queryService.mergeFactNode(
                QueryRequest.forFactNode(parentId, answer.getId(), props)
        );

        handleQuestionRelationship(nodeId, questionId, roleNodeId);
        handleRuleProcessing(nodeId, FACT_ROLE, roleNodeId);
        handleEventProcessing(nodeId, answerValueId, execution.getConditionCharacteristics(), roleNodeId);
        handleSubQuestions(execution, answerValueId, nodeId, questions, questionAnswers, roleNodeId);
    }

    private void handleQuestionRelationship(Long nodeId, Long questionId, Map<String, List<Long>> roleNodeId) {
        queryService.buildRelationship(QueryRequest.forRelationship(nodeId, questionId, FACT_QUESTION_LINK));
        queryService.buildRelationship(QueryRequest.forRelationship(roleNodeId.get(RESPONSE_SESSION_ROLE).get(0), questionId, SESSION_QUESTION_LINK));
        queryService.buildRelationship(QueryRequest.forRelationship(roleNodeId.get(SUBJECT_ROLE).get(0), questionId, SUBJECT_QUESTION_LINK));
    }

    private void handleEventProcessing(Long factNodeId, Long answerValueId, List<ConditionCharacteristicDTO> conditionCharacteristics, Map<String, List<Long>> roleNodeId) {
        QueryRequest request = QueryRequest.forEventNode(factNodeId);
        queryService.delete(request);
        if (!isEmpty(conditionCharacteristics)) {
            conditionCharacteristics
                    .stream()
                    .filter(cc -> cc.getConditionAnswerId().equals(answerValueId))
                    .findFirst()
                    .ifPresent(cc -> {
                        request.setEventTypeId(cc.getOuterRiskEventId());
                        Long eventId = queryService.createEvent(request);
                        handleRuleProcessing(eventId, EVENT_ROLE, roleNodeId);
                    });
        }
    }

    private void handleSubQuestions(QuestionExecutionDTO execution,
                                    Long answerValueId,
                                    Long parentId,
                                    Map<Long, QuestionExecutionDTO> questions,
                                    Map<Long, AnswerDTO> questionAnswers,
                                    Map<String, List<Long>> roleNodeId) {
        if (!isEmpty(execution.getSubQuestions())) {
            execution.getSubQuestions().forEach(sub -> {
                List<Long> subQuestionIds = sub.getConditionAnswerQuestionIds().get(answerValueId);
                if (!isEmpty(subQuestionIds)) {
                    subQuestionIds
                            .forEach(subQ -> handleQuestion(questions.get(subQ), parentId, questions, questionAnswers, roleNodeId));
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

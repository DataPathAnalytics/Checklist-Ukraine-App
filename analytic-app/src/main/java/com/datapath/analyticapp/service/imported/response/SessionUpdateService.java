package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.entity.imported.ResponseSessionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.exception.ValidationException;
import com.datapath.analyticapp.service.db.DatabaseUtils;
import com.datapath.analyticapp.service.db.QueryRequest;
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

    public SessionUpdateService(DatabaseUtils dbUtils,
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

        dbUtils.buildRelationship(QueryRequest.builder()
                .parentId(roleNodeId.get(CONTROL_ACTIVITY_ROLE).get(0))
                .childId(savedId)
                .relType(ACTIVITY_SESSION_LINK)
                .build());
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

        Long nodeId = dbUtils.mergeIdentifierNode(
                QueryRequest.builder()
                        .parentNodeType(nodeType)
                        .identifierField(identifier.getName())
                        .identifierValue(answerValue.get(identifier.getName()))
                        .identifierType(identifier.getValueType())
                        .params(answerValue)
                        .build());

        handleRuleProcessing(nodeId, SUBJECT_ROLE, roleNodeId);
        handleSubQuestions(subjectQuestion, nodeId, questions, questionAnswers, roleNodeId);
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
                .forEach(execution -> {
                    Map<String, Object> answerValues = questionAnswers.get(execution.getId()).getValues();
//                    if (!isEmpty(answerValues)) {
//                        handleQ(execution, roleNodeId.get(RESPONSE_SESSION_ROLE).get(0));
//
//                        if (isFact(execution)) {
//                            handleFactQuestion(execution, answerValues, roleNodeId);
//                        } else {
//                            handleNotFactQuestion(execution, answerValues, roleNodeId);
//                        }
//                    }
                });
    }

//    private void handleQ(QuestionExecutionDTO execution,
//                                Long parentId,
//                                Map<Long, QuestionExecutionDTO> questions,
//                                Map<Long, AnswerDTO> questionAnswers,
//                                Map<String, List<Long>> roleNodeId) {
//        if (isFact(execution)) {
//            handleFactQuestion(execution, answerValues, roleNodeId);
//        } else {
//            handleNotFactQuestion(execution, answerValues, roleNodeId);
//        }
//    }

    private void handleFactQuestion(QuestionExecutionDTO execution, Map<String, Object> answerValues, Map<String, List<Long>> roleNodeId) {

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

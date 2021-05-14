package com.datapath.analyticapp.service.miner.handler;

import com.datapath.analyticapp.dao.entity.RoleEntity;
import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import com.datapath.analyticapp.dao.repository.KnowledgeClassRepository;
import com.datapath.analyticapp.dao.repository.QuestionRepository;
import com.datapath.analyticapp.dao.repository.RoleRepository;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.service.miner.config.Place;
import com.datapath.analyticapp.service.miner.rule.MinerRule;
import com.datapath.analyticapp.service.miner.rule.MinerRuleProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.datapath.analyticapp.dao.Node.UNKNOWN_NODE;
import static com.datapath.analyticapp.service.miner.config.Role.FEATURE_ROLE;
import static com.datapath.analyticapp.service.miner.config.Role.INITIATOR_ROLE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class BaseUpdateHandler {

    protected final CypherQueryService queryService;
    protected final MinerRuleProvider minerRuleProvider;
    protected final RoleRepository roleRepository;
    protected final UserRepository userRepository;
    protected final QuestionRepository questionRepository;
    protected final KnowledgeClassRepository knowledgeClassRepository;
    protected final QueryRequestBuilder queryRequestBuilder;
    protected Map<String, List<Long>> roleNodeIdMap;

    public BaseUpdateHandler(CypherQueryService queryService,
                             MinerRuleProvider minerRuleProvider,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             QuestionRepository questionRepository,
                             KnowledgeClassRepository knowledgeClassRepository,
                             QueryRequestBuilder queryRequestBuilder) {
        this.queryService = queryService;
        this.minerRuleProvider = minerRuleProvider;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.knowledgeClassRepository = knowledgeClassRepository;
        this.queryRequestBuilder = queryRequestBuilder;
    }

    protected Long getUpdatedQuestionId(QuestionDTO question) {
        return questionRepository.findFirstByOuterId(question.getId())
                .orElseGet(() -> {
                    QuestionEntity entity = new QuestionEntity();
                    entity.setOuterId(question.getId());
                    entity.setValue(question.getValue());
                    entity.setDateCreated(question.getDateCreated());
                    entity.setKnowledgeClasses(knowledgeClassRepository.findAllById(question.getKnowledgeClasses()));
                    return questionRepository.save(entity);
                }).getId();
    }

    protected void handleTypeQuestions(SessionDTO session, String parentRoleId) {
        Map<Long, AnswerDTO> questionAnswers = session.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questionMap = session.getTemplate().getTypeQuestions()
                .stream()
                .filter(q -> !q.isRoot())
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        session.getTemplate().getTypeQuestions()
                .stream()
                .filter(QuestionExecutionDTO::isRoot)
                .forEach(execution -> {
                    Map<String, Object> answerValues = questionAnswers.get(execution.getId()).getValues();
                    if (!isEmpty(answerValues)) {
                        handleQuestion(
                                execution,
                                roleNodeIdMap.get(parentRoleId).get(0),
                                questionMap,
                                questionAnswers);
                    }
                });
    }

    protected void handleQuestion(QuestionExecutionDTO execution,
                                  Long parentId,
                                  Map<Long, QuestionExecutionDTO> questions,
                                  Map<Long, AnswerDTO> questionAnswers) {
        if (UNKNOWN_NODE.equals(execution.getNodeType())) return;

        AnswerDTO answer = questionAnswers.get(execution.getId());
        if (isNull(answer)) return;
        Map<String, Object> answerValue = answer.getValues();
        if (isEmpty(answerValue)) return;

        Optional<FieldDescriptionDTO> identifier = getIdentifierField(execution);

        Long nodeId;
        if (!identifier.isPresent()) {
            nodeId = queryService.mergeNotIdentifierNode(
                    queryRequestBuilder.nonIdentifierRequest(parentId,
                            execution.getNodeType(),
                            getLinkTypeOrBasedOnNodeType(execution),
                            answerValue,
                            getFieldTypes(execution),
                            getInitiatorId())
            );
        } else {
            nodeId = queryService.mergeIdentifierNode(
                    queryRequestBuilder.identifierRequest(
                            execution.getNodeType(),
                            identifier.get().getName(),
                            answerValue.get(identifier.get().getName()),
                            answerValue,
                            getFieldTypes(execution))
            );
            queryService.buildRelationship(
                    queryRequestBuilder.relationshipRequest(
                            parentId,
                            nodeId,
                            getLinkTypeOrBasedOnNodeType(execution),
                            getInitiatorId()
                    )
            );
        }

        addRoleNodeId(FEATURE_ROLE, nodeId);
        addRoleNodeId(execution, nodeId);

        handleSubQuestions(execution, nodeId, questions, questionAnswers);
    }

    protected void addRoleNodeId(String roleName, Long nodeId) {
        roleNodeIdMap.computeIfPresent(roleName, (k, v) -> {
                    v.add(nodeId);
                    return v;
                }
        );
        roleNodeIdMap.computeIfAbsent(roleName, k -> {
            ArrayList<Long> v = new ArrayList<>();
            v.add(nodeId);
            return v;
        });
    }

    protected void addRoleNodeId(QuestionExecutionDTO execution, Long nodeId) {
        if (nonNull(execution.getRoleId())) {
            Optional<RoleEntity> roleEntity = roleRepository.findById(execution.getRoleId());
            roleEntity.ifPresent(role -> addRoleNodeId(role.getRoleName(), nodeId));
        }
    }

    protected void handleSubQuestions(QuestionExecutionDTO execution,
                                      Long parentId,
                                      Map<Long, QuestionExecutionDTO> questions,
                                      Map<Long, AnswerDTO> questionAnswers) {
        if (!isEmpty(execution.getSubQuestions())) {
            execution.getSubQuestions().forEach(sub -> {
                if (conditionMet(sub, questionAnswers.get(execution.getId()))) {
                    sub.getQuestionIds()
                            .forEach(subQ -> handleQuestion(questions.get(subQ), parentId, questions, questionAnswers));
                }
            });
        }
    }

    protected Optional<FieldDescriptionDTO> getIdentifierField(QuestionExecutionDTO q) {
        return q.getQuestion().getAnswerStructure().getFieldDescriptions()
                .stream()
                .filter(FieldDescriptionDTO::isIdentifier)
                .findFirst();
    }

    protected boolean conditionMet(SubQuestionDTO sub, AnswerDTO answer) {
        return isNull(sub.getConditionFieldName()) || nonNull(answer.getValues().get(sub.getConditionFieldName()));
    }

    protected void handleRuleMining(Place place) {
        minerRuleProvider.getRulesByPlace(place).forEach(this::processRule);
    }

    private void processRule(MinerRule rule) {
        List<Long> nodeIds = roleNodeIdMap.get(rule.getRole());

        if (isEmpty(nodeIds)) return;

        List<Long> secondNodeId = roleNodeIdMap.get(rule.getSecondNodeType());
        List<Long> parentOfSecondNodeId = roleNodeIdMap.get(rule.getParentOfSecondNode());

        nodeIds.forEach(nodeId -> queryService.buildRelationshipUseRule(
                queryRequestBuilder.ruleRelationshipRequest(
                        rule,
                        nodeId,
                        secondNodeId,
                        parentOfSecondNodeId,
                        rule.getParentOfSecondNodeLinkType(),
                        getInitiatorId())
                )
        );
    }

    protected Map<String, ValueType> getFieldTypes(QuestionExecutionDTO execution) {
        return execution.getQuestion().getAnswerStructure().getFieldDescriptions()
                .stream()
                .collect(toMap(FieldDescriptionDTO::getName, FieldDescriptionDTO::getValueType));
    }

    protected Long getInitiatorId() {
        return roleNodeIdMap.get(INITIATOR_ROLE).get(0);
    }

    protected String getNodeTypeOrDefault(QuestionExecutionDTO execution, String defaultNodeType) {
        return UNKNOWN_NODE.equals(execution.getNodeType()) ?
                defaultNodeType :
                execution.getNodeType();
    }

    protected String getLinkTypeOrBasedOnNodeType(QuestionExecutionDTO execution) {
        return StringUtils.hasText(execution.getLinkType()) ?
                execution.getLinkType() :
                "HAS_" + execution.getNodeType().toUpperCase();
    }
}

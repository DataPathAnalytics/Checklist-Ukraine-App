package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.LinkTypeEntity;
import com.datapath.analyticapp.dao.entity.NodeTypeEntity;
import com.datapath.analyticapp.dao.entity.RoleEntity;
import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.service.miner.MinerRule;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import com.datapath.analyticapp.service.miner.config.MinerRulePlace;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.datapath.analyticapp.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class BaseUpdateService {

    protected final CypherQueryService queryService;
    protected final MinerRuleProvider minerRuleProvider;
    protected final RoleRepository roleRepository;
    protected final NodeTypeRepository nodeTypeRepository;
    protected final UserRepository userRepository;
    protected final QuestionRepository questionRepository;
    protected final KnowledgeClassRepository knowledgeClassRepository;
    protected Map<String, List<Long>> roleNodeIdMap;

    public BaseUpdateService(CypherQueryService queryService,
                             MinerRuleProvider minerRuleProvider,
                             RoleRepository roleRepository,
                             NodeTypeRepository nodeTypeRepository,
                             UserRepository userRepository,
                             QuestionRepository questionRepository,
                             KnowledgeClassRepository knowledgeClassRepository) {
        this.queryService = queryService;
        this.minerRuleProvider = minerRuleProvider;
        this.roleRepository = roleRepository;
        this.nodeTypeRepository = nodeTypeRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.knowledgeClassRepository = knowledgeClassRepository;
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
        AnswerDTO answer = questionAnswers.get(execution.getId());
        if (isNull(answer)) return;
        Map<String, Object> answerValue = answer.getValues();
        if (isEmpty(answerValue)) return;

        Optional<FieldDescriptionDTO> identifier = getIdentifierField(execution);

        String nodeType;
        String linkType;

        Optional<NodeTypeEntity> nodeTypeEntity = getNodeType(execution);
        if (nodeTypeEntity.isPresent()) {
            nodeType = nodeTypeEntity.get().getNodeTypeName();
            linkType = getLinkType(execution, nodeTypeEntity.get()).orElse(DEFAULT_LINK);
        } else {
            nodeType = DEFAULT_NODE;
            linkType = DEFAULT_LINK;
        }

        Long nodeId;
        if (!identifier.isPresent()) {
            nodeId = queryService.mergeNotIdentifierNode(
                    QueryRequestBuilder.nonIdentifierRequest(parentId, nodeType, linkType, answerValue)
            );
        } else {
            nodeId = queryService.mergeIdentifierNode(
                    QueryRequestBuilder.identifierRequest(
                            nodeType,
                            identifier.get().getName(),
                            answerValue.get(identifier.get().getName()),
                            identifier.get().getValueType(),
                            answerValue)
            );
            queryService.buildRelationship(QueryRequestBuilder.relationshipRequest(parentId, nodeId, linkType));
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

    protected Optional<NodeTypeEntity> getNodeType(QuestionExecutionDTO execution) {
        if (isNull(execution.getNodeTypeId())) return Optional.empty();
        return nodeTypeRepository.findById(execution.getNodeTypeId());
    }

    protected String getNodeType(QuestionExecutionDTO execution, String defaultNodeType) {
        if (isNull(execution.getNodeTypeId())) return defaultNodeType;
        Optional<NodeTypeEntity> nodeTypeEntity = nodeTypeRepository.findById(execution.getNodeTypeId());
        return nodeTypeEntity.map(NodeTypeEntity::getNodeTypeName).orElse(defaultNodeType);
    }

    protected Optional<String> getLinkType(QuestionExecutionDTO execution, NodeTypeEntity nodeType) {
        if (isNull(execution.getLinkTypeId())) return Optional.empty();
        return nodeType.getLinkTypes()
                .stream()
                .filter(l -> l.getId().equals(execution.getLinkTypeId()))
                .findFirst()
                .map(LinkTypeEntity::getLinkTypeName);
    }

    protected boolean conditionMet(SubQuestionDTO sub, AnswerDTO answer) {
        return isNull(sub.getConditionFieldName()) || nonNull(answer.getValues().get(sub.getConditionFieldName()));
    }

    protected void handleRuleMining(MinerRulePlace place) {
        minerRuleProvider.getRulesByPlace(place).forEach(this::processRule);
    }

    private void processRule(MinerRule rule) {
        List<Long> nodeIds = roleNodeIdMap.get(rule.getRole());

        if (isEmpty(nodeIds)) return;

        List<Long> secondNodeId = roleNodeIdMap.get(rule.getSecondNodeType());
        List<Long> parentOfSecondNodeId = roleNodeIdMap.get(rule.getParentOfSecondNode());

        nodeIds.forEach(nodeId -> queryService.buildRelationshipUseRule(
                QueryRequestBuilder.ruleRelationshipRequest(
                        rule,
                        nodeId,
                        secondNodeId,
                        parentOfSecondNodeId,
                        rule.getParentOfSecondNodeLinkType())
                )
        );
    }
}

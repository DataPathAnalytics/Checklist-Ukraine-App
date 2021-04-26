package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.LinkTypeEntity;
import com.datapath.analyticapp.dao.entity.NodeTypeEntity;
import com.datapath.analyticapp.dao.entity.RoleEntity;
import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.service.db.DatabaseUtils;
import com.datapath.analyticapp.service.db.QueryRequest;
import com.datapath.analyticapp.service.miner.MinerRule;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.datapath.analyticapp.Constants.DEFAULT_LINK;
import static com.datapath.analyticapp.Constants.DEFAULT_NODE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class BaseUpdateService {


    protected final DatabaseUtils dbUtils;
    protected final MinerRuleProvider minerRuleProvider;
    protected final RoleRepository roleRepository;
    protected final NodeTypeRepository nodeTypeRepository;
    protected final UserRepository userRepository;
    protected final QuestionRepository questionRepository;
    protected final KnowledgeClassRepository knowledgeClassRepository;

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

    protected void handleTypeQuestions(SessionDTO session, Map<String, List<Long>> roleNodeId, String parentRoleId) {
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
                                roleNodeId.get(parentRoleId).get(0),
                                questionMap,
                                questionAnswers,
                                roleNodeId);
                    }
                });
    }

    protected void handleQuestion(QuestionExecutionDTO execution,
                                  Long parentId,
                                  Map<Long, QuestionExecutionDTO> questions,
                                  Map<Long, AnswerDTO> questionAnswers,
                                  Map<String, List<Long>> roleNodeId) {
        //TODO:needs solve question about question relationship
        //Long questionId = getUpdatedQuestionId(execution.getQuestion());

        Map<String, Object> answerValues = questionAnswers.get(execution.getId()).getValues();
        if (isEmpty(answerValues)) return;

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
            nodeId = dbUtils.updateNotIdentifierNode(
                    QueryRequest.builder()
                            .parentId(parentId)
                            .relType(linkType)
                            .childNodeType(nodeType)
                            .params(answerValues)
                            .build());
        } else {
            nodeId = dbUtils.mergeIdentifierNode(
                    QueryRequest.builder()
                            .parentId(parentId)
                            .parentNodeType(nodeType)
                            .identifierField(identifier.get().getName())
                            .identifierValue(answerValues.get(identifier.get().getName()))
                            .identifierType(identifier.get().getValueType())
                            .params(answerValues)
                            .build());
            dbUtils.buildRelationship(QueryRequest.builder()
                    .parentId(parentId)
                    .childId(nodeId)
                    .relType(linkType)
                    .build());
        }

        handleRuleProcessing(execution, nodeId, roleNodeId);
        handleSubQuestions(execution, nodeId, questions, questionAnswers, roleNodeId);
    }

    protected void handleRuleProcessing(Long nodeId, String roleName, Map<String, List<Long>> roleNodeId) {
        roleNodeId.computeIfPresent(roleName, (k, v) -> {
                    v.add(nodeId);
                    return v;
                }
        );
        roleNodeId.computeIfAbsent(roleName, k -> Arrays.asList(nodeId));

        List<MinerRule> minerRules = minerRuleProvider.getByRole(roleName);
        minerRules.forEach(rule -> {
            if (nonNull(rule.getSecondNodeType())) {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getSecondNodeType()));
            } else {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getParentOfSecondNode()), rule.getParentOfSecondNodeLinkType());
            }
        });
    }

    protected void handleRuleProcessing(QuestionExecutionDTO execution, Long nodeId, Map<String, List<Long>> roleNodeId) {
        if (nonNull(execution.getRoleId())) {
            Optional<RoleEntity> roleEntity = roleRepository.findById(execution.getRoleId());

            roleNodeId.computeIfPresent(roleEntity.get().getRoleName(), (k, v) -> {
                        v.add(nodeId);
                        return v;
                    }
            );
            roleNodeId.computeIfAbsent(roleEntity.get().getRoleName(), k -> Arrays.asList(nodeId));

            List<MinerRule> minerRules = minerRuleProvider.getByRole(roleEntity.get().getRoleName());
            minerRules.forEach(rule -> {
                if (nonNull(rule.getSecondNodeType())) {
                    dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getSecondNodeType()));
                } else {
                    dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getParentOfSecondNode()), rule.getParentOfSecondNodeLinkType());
                }
            });
        }
    }

    protected void handleSubQuestions(QuestionExecutionDTO execution,
                                      Long parentId,
                                      Map<Long, QuestionExecutionDTO> questions,
                                      Map<Long, AnswerDTO> questionAnswers,
                                      Map<String, List<Long>> roleNodeId) {
        if (!isEmpty(execution.getSubQuestions())) {
            execution.getSubQuestions().forEach(sub -> {
                if (conditionMet(sub, questionAnswers.get(execution.getId()))) {
                    sub.getQuestionIds()
                            .forEach(subQ -> handleQuestion(questions.get(subQ), parentId, questions, questionAnswers, roleNodeId));
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

    private Optional<String> getLinkType(QuestionExecutionDTO execution, NodeTypeEntity nodeType) {
        if (isNull(execution.getLinkTypeId())) return Optional.empty();
        return nodeType.getLinkTypes()
                .stream()
                .filter(l -> l.getId().equals(execution.getLinkTypeId()))
                .findFirst()
                .map(LinkTypeEntity::getLinkTypeName);
    }

    private boolean conditionMet(SubQuestionDTO sub, AnswerDTO answer) {
        return isNull(sub.getConditionFieldName()) || nonNull(answer.getValues().get(sub.getConditionFieldName()));
    }
}

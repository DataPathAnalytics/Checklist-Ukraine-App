package com.datapath.analyticapp.service.imported.activity;

import com.datapath.analyticapp.dao.entity.LinkTypeEntity;
import com.datapath.analyticapp.dao.entity.NodeTypeEntity;
import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.entity.imported.QuestionEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.service.db.DatabaseUtils;
import com.datapath.analyticapp.service.db.ParentNodeData;
import com.datapath.analyticapp.service.db.QueryRequest;
import com.datapath.analyticapp.service.miner.MinerRule;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.datapath.analyticapp.Constants.AUTHORITY_REPRESENTATIVE_ROLE;
import static com.datapath.analyticapp.Constants.OWNER_ROLE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityUpdateService {

    private final static String DEFAULT_NODE = "Data";
    private final static String AUTHORITY_DEFAULT_NODE = "Authority";
    private final static String OWNER_DEFAULT_NODE = "Owner";
    private final static String DEFAULT_LINK = "HAS_DATA";

    private final ControlActivityRepository controlActivityRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final KnowledgeClassRepository knowledgeClassRepository;
    private final NodeTypeRepository nodeTypeRepository;
    private final MinerRuleProvider minerRuleProvider;
    private final DatabaseUtils dbUtils;

    @Transactional
    public void update(ResponseDataDTO response) {
        log.info("Updating control activity {}", response.getId());

        if (response.getActivity().isInvalid()) {
            //TODO:handle remove logic
        }

        Map<String, Long> roleNodeId = new HashMap<>();

        Long controlActivityId = handleControlActivity(response);
        roleNodeId.put("ControlActivity", controlActivityId);

        handleAuthority(response.getActivity(), roleNodeId);

        handleOwner(response.getActivity(), roleNodeId);
        handleTypeQuestions(response.getActivity(), controlActivityId);
    }

    private void handleAuthority(SessionDTO activity, Map<String, Long> roleNodeId) {
        Map<Long, AnswerDTO> questionAnswers = activity.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = activity.getTemplate().getAuthorityFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        QuestionExecutionDTO authorityQuestion = activity.getTemplate().getAuthorityQuestion();
        Map<String, Object> answerValue = questionAnswers.get(authorityQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        Optional<String> identifierName = getIdentifierName(authorityQuestion);

        String nodeType = AUTHORITY_DEFAULT_NODE;
        Optional<NodeTypeEntity> nodeTypeEntity = getNodeType(authorityQuestion);
        if (nodeTypeEntity.isPresent()) {
            nodeType = nodeTypeEntity.get().getNodeTypeName();
        }

        Long nodeId = dbUtils.mergeIdentifierNode(
                QueryRequest.builder()
                        .nodeType(nodeType)
                        .identifierField(identifierName.get())
                        .identifierValue(answerValue.get(identifierName.get()))
                        .params(answerValue)
                        .build()
        );

        List<MinerRule> minerRules = minerRuleProvider.getByRole(AUTHORITY_REPRESENTATIVE_ROLE);
        minerRules.forEach(rule -> {
            if (nonNull(rule.getSecondNodeType())) {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getSecondNodeType()));
            } else {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getParentOfSecondNode()), rule.getParentOfSecondNodeLinkType());
            }
        });

        if (!isEmpty(authorityQuestion.getSubQuestions())) {
            authorityQuestion.getSubQuestions().forEach(sub -> {
                if (isNull(sub.getConditionFieldName())) {
                    sub.getQuestionIds()
                            .forEach(subQ ->
                                    handleQuestion(
                                            questions.get(subQ),
                                            ParentNodeData.builder().id(nodeId).build(),
                                            questions,
                                            questionAnswers)
                            );
                }
            });
        }
    }

    private void handleOwner(SessionDTO activity, Map<String, Long> roleNodeId) {
        Map<Long, AnswerDTO> questionAnswers = activity.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = activity.getTemplate().getAuthorityFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        QuestionExecutionDTO ownerQuestion = activity.getTemplate().getObjectQuestion();
        Map<String, Object> answerValue = questionAnswers.get(ownerQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        Optional<String> identifierName = getIdentifierName(ownerQuestion);

        String nodeType = OWNER_DEFAULT_NODE;
        Optional<NodeTypeEntity> nodeTypeEntity = getNodeType(ownerQuestion);
        if (nodeTypeEntity.isPresent()) {
            nodeType = nodeTypeEntity.get().getNodeTypeName();
        }

        Long nodeId = dbUtils.mergeIdentifierNode(
                QueryRequest.builder()
                        .nodeType(nodeType)
                        .identifierField(identifierName.get())
                        .identifierValue(answerValue.get(identifierName.get()))
                        .params(answerValue)
                        .build()
        );

        List<MinerRule> minerRules = minerRuleProvider.getByRole(OWNER_ROLE);
        minerRules.forEach(rule -> {
            if (nonNull(rule.getSecondNodeType())) {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getSecondNodeType()));
            } else {
                dbUtils.buildRelationshipUseRule(rule, nodeId, roleNodeId.get(rule.getParentOfSecondNode()), rule.getParentOfSecondNodeLinkType());
            }
        });

        if (!isEmpty(ownerQuestion.getSubQuestions())) {
            ownerQuestion.getSubQuestions().forEach(sub -> {
                if (isNull(sub.getConditionFieldName())) {
                    sub.getQuestionIds()
                            .forEach(subQ ->
                                    handleQuestion(
                                            questions.get(subQ),
                                            ParentNodeData.builder().id(nodeId).build(),
                                            questions,
                                            questionAnswers)
                            );
                }
            });
        }
    }

    private void handleTypeQuestions(SessionDTO activity, Long controlActivityId) {
        Map<Long, AnswerDTO> questionAnswers = activity.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questionMap = activity.getTemplate().getTypeQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        activity.getTemplate().getTypeQuestions()
                .stream()
                .filter(QuestionExecutionDTO::isRoot)
                .forEach(execution -> {
                    Map<String, Object> answerValues = questionAnswers.get(execution.getId()).getValues();
                    if (!isEmpty(answerValues)) {
                        handleQuestion(
                                execution,
                                ParentNodeData.builder().id(controlActivityId).build(),
                                questionMap,
                                questionAnswers);
                    }
                });
    }

    private void handleQuestion(QuestionExecutionDTO execution,
                                ParentNodeData parent,
                                Map<Long, QuestionExecutionDTO> questions,
                                Map<Long, AnswerDTO> questionAnswers) {
        //TODO:needs solve question about question relationship
        //Long questionId = getUpdatedQuestionId(execution.getQuestion());

        Map<String, Object> answerValues = questionAnswers.get(execution.getId()).getValues();
        if (isEmpty(answerValues)) return;

        Optional<String> identifierName = getIdentifierName(execution);

        String nodeType = DEFAULT_NODE;
        String linkType = DEFAULT_LINK;

        Optional<NodeTypeEntity> nodeTypeEntity = getNodeType(execution);
        if (nodeTypeEntity.isPresent()) {
            nodeType = nodeTypeEntity.get().getNodeTypeName();
            linkType = getLinkType(execution, nodeTypeEntity.get()).orElse(DEFAULT_LINK);
        }

        Long nodeId;
        if (!identifierName.isPresent()) {
            nodeId = dbUtils.updateNotIdentifierNode(
                    QueryRequest.builder()
                            .id(parent.getId())
                            .relType(linkType)
                            .addNodeType(nodeType)
                            .params(answerValues)
                            .build());
        } else {
            nodeId = dbUtils.updateIdentifierNode(
                    QueryRequest.builder()
                            .id(parent.getId())
                            .relType(linkType)
                            .addNodeType(nodeType)
                            .identifierField(identifierName.get())
                            .identifierValue(answerValues.get(identifierName.get()))
                            .params(answerValues)
                            .build());
        }

        //TODO:add role processing

        if (!isEmpty(execution.getSubQuestions())) {
            execution.getSubQuestions().forEach(sub -> {
                if (isNull(sub.getConditionFieldName())) {
                    sub.getQuestionIds()
                            .forEach(subQ ->
                                    handleQuestion(
                                            questions.get(subQ),
                                            ParentNodeData.builder().id(nodeId).build(),
                                            questions,
                                            questionAnswers)
                            );
                }
            });
        }
    }

    private Long getUpdatedQuestionId(QuestionDTO question) {
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

    private Long handleControlActivity(ResponseDataDTO response) {
        ControlActivityEntity activity = controlActivityRepository.findByOuterId(response.getId());

        if (isNull(activity)) {
            activity = new ControlActivityEntity();
            activity.setOuterId(response.getId());
            activity.setDateCreated(response.getActivity().getDateCreated());
            setAuthor(activity, response.getActivity().getAuthorId());
        }
        setMembers(activity, response.getActivity().getMembers());
//        activity.setDateModified(response.getActivity().getDateModified());
        return controlActivityRepository.save(activity).getId();
    }

    private void setMembers(ControlActivityEntity activity, List<Long> members) {
        activity.setMembers(userRepository.findAllByOuterIdIn(members));
    }

    private void setAuthor(ControlActivityEntity activity, Long authorId) {
        activity.setAuthor(userRepository.findByOuterId(authorId));
    }

    private Optional<String> getIdentifierName(QuestionExecutionDTO q) {
        return q.getQuestion().getAnswerStructure().getFieldDescriptions()
                .stream()
                .filter(FieldDescriptionDTO::isIdentifier)
                .findFirst()
                .map(FieldDescriptionDTO::getName);
    }

    private Optional<NodeTypeEntity> getNodeType(QuestionExecutionDTO execution) {
        if (isNull(execution.getNodeTypeId())) return Optional.empty();
        return nodeTypeRepository.findById(execution.getNodeTypeId());
    }

    private Optional<String> getLinkType(QuestionExecutionDTO execution, NodeTypeEntity nodeType) {
        if (isNull(execution.getLinkTypeId())) return Optional.empty();
        return nodeType.getLinkTypes()
                .stream()
                .filter(l -> l.getId().equals(execution.getLinkTypeId()))
                .findFirst()
                .map(LinkTypeEntity::getLinkTypeName);
    }
}

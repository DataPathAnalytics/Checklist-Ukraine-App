package com.datapath.analyticapp.service.imported.response;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
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
public class ActivityUpdateService extends BaseUpdateService {

    private final ControlActivityRepository controlActivityRepository;

    public ActivityUpdateService(ControlActivityRepository controlActivityRepository,
                                 UserRepository userRepository,
                                 QuestionRepository questionRepository,
                                 KnowledgeClassRepository knowledgeClassRepository,
                                 NodeTypeRepository nodeTypeRepository,
                                 MinerRuleProvider minerRuleProvider,
                                 CypherQueryService dbUtils,
                                 RoleRepository roleRepository,
                                 QueryRequestBuilder queryRequestBuilder) {
        super(dbUtils,
                minerRuleProvider,
                roleRepository,
                nodeTypeRepository,
                userRepository,
                questionRepository,
                knowledgeClassRepository,
                queryRequestBuilder);
        this.controlActivityRepository = controlActivityRepository;
    }

    @Transactional
    public void update(ResponseDataDTO response) {
        log.info("Updating control activity {}", response.getId());

        //TODO:handle remove logic
        if (response.getActivity().isInvalid()) {
//            deleteActivityCurrentState(response.getId());
//            return;
        }

        roleNodeIdMap = new HashMap<>();

        handleControlActivity(response);
        handleTypeQuestions(response.getActivity(), CONTROL_ACTIVITY_ROLE);
        handleAuthority(response.getActivity());
        handleOwner(response.getActivity());
        handleRuleMining(MinerRulePlace.control_activity);
    }

    private void handleControlActivity(ResponseDataDTO response) {
        ControlActivityEntity activity = controlActivityRepository.findByOuterId(response.getId());

        if (isNull(activity)) {
            activity = new ControlActivityEntity();
            activity.setOuterId(response.getId());
            activity.setDateCreated(response.getActivity().getDateCreated());
            setAuthor(activity, response.getActivity().getAuthorId());
        }
        setMembers(activity, response.getActivity().getMembers());
//        activity.setDateModified(response.getActivity().getDateModified());

        addRoleNodeId(CONTROL_ACTIVITY_ROLE, controlActivityRepository.save(activity).getId());
    }

    private void handleAuthority(SessionDTO activity) {
        Map<Long, AnswerDTO> questionAnswers = activity.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = activity.getTemplate().getAuthorityFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        QuestionExecutionDTO authorityQuestion = activity.getTemplate().getAuthorityQuestion();
        Map<String, Object> answerValue = questionAnswers.get(authorityQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        FieldDescriptionDTO identifier = getIdentifierField(authorityQuestion)
                .orElseThrow(() -> new ValidationException("Authority must have identifier field"));

        String nodeType = getNodeType(authorityQuestion, AUTHORITY_DEFAULT_NODE);

        Long nodeId = queryService.mergeIdentifierNode(
                queryRequestBuilder.identifierRequest(
                        nodeType,
                        identifier.getName(),
                        answerValue.get(identifier.getName()),
                        answerValue,
                        getFieldTypes(authorityQuestion))
        );
        addRoleNodeId(AUTHORITY_REPRESENTATIVE_ROLE, nodeId);

        handleSubQuestions(authorityQuestion, nodeId, questions, questionAnswers);
    }

    private void handleOwner(SessionDTO activity) {
        Map<Long, AnswerDTO> questionAnswers = activity.getAnswers().stream()
                .collect(toMap(AnswerDTO::getQuestionId, Function.identity()));

        Map<Long, QuestionExecutionDTO> questions = activity.getTemplate().getAuthorityFeatureQuestions()
                .stream()
                .collect(toMap(QuestionExecutionDTO::getId, Function.identity()));

        QuestionExecutionDTO ownerQuestion = activity.getTemplate().getObjectQuestion();
        Map<String, Object> answerValue = questionAnswers.get(ownerQuestion.getId()).getValues();
        if (isEmpty(answerValue)) return;

        FieldDescriptionDTO identifier = getIdentifierField(ownerQuestion)
                .orElseThrow(() -> new ValidationException("Owner must have identifier field"));

        String nodeType = getNodeType(ownerQuestion, OWNER_DEFAULT_NODE);

        Long nodeId = queryService.mergeIdentifierNode(
                queryRequestBuilder.identifierRequest(
                        nodeType,
                        identifier.getName(),
                        answerValue.get(identifier.getName()),
                        answerValue,
                        getFieldTypes(ownerQuestion))
        );
        addRoleNodeId(OWNER_ROLE, nodeId);

        handleSubQuestions(ownerQuestion, nodeId, questions, questionAnswers);
    }

    private void setMembers(ControlActivityEntity activity, List<Long> members) {
        activity.setMembers(userRepository.findAllByOuterIdIn(members));
    }

    private void setAuthor(ControlActivityEntity activity, Long authorId) {
        activity.setAuthor(userRepository.findByOuterId(authorId));
    }
}

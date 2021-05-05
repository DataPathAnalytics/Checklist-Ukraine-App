package com.datapath.analyticapp.service.miner.handler;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.repository.*;
import com.datapath.analyticapp.dao.service.CypherQueryService;
import com.datapath.analyticapp.dao.service.QueryRequestBuilder;
import com.datapath.analyticapp.dto.imported.response.*;
import com.datapath.analyticapp.exception.ValidationException;
import com.datapath.analyticapp.service.miner.MinerRuleProvider;
import com.datapath.analyticapp.service.miner.config.Place;
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
public class ActivityUpdateHandler extends BaseUpdateHandler {

    private final ControlActivityRepository controlActivityRepository;
    private final SessionUpdateHandler sessionUpdateHandler;

    public ActivityUpdateHandler(ControlActivityRepository controlActivityRepository,
                                 UserRepository userRepository,
                                 QuestionRepository questionRepository,
                                 KnowledgeClassRepository knowledgeClassRepository,
                                 NodeTypeRepository nodeTypeRepository,
                                 MinerRuleProvider minerRuleProvider,
                                 CypherQueryService cypherQueryService,
                                 RoleRepository roleRepository,
                                 QueryRequestBuilder queryRequestBuilder,
                                 SessionUpdateHandler sessionUpdateHandler) {
        super(cypherQueryService,
                minerRuleProvider,
                roleRepository,
                nodeTypeRepository,
                userRepository,
                questionRepository,
                knowledgeClassRepository,
                queryRequestBuilder);
        this.controlActivityRepository = controlActivityRepository;
        this.sessionUpdateHandler = sessionUpdateHandler;
    }

    @Transactional
    public void update(ControlActivityDTO controlActivityDTO) {
        log.info("Updating control activity {}", controlActivityDTO.getId());
        delete(controlActivityDTO);
        save(controlActivityDTO);
    }

    private void save(ControlActivityDTO controlActivityDTO) {
        if (controlActivityDTO.getActivity().isInvalid()) {
            return;
        }

        roleNodeIdMap = new HashMap<>();

        handleControlActivity(controlActivityDTO);
        handleTypeQuestions(controlActivityDTO.getActivity(), CONTROL_ACTIVITY_ROLE);
        handleAuthority(controlActivityDTO.getActivity());
        handleOwner(controlActivityDTO.getActivity());
        handleRuleMining(Place.control_activity);

        controlActivityDTO.getSessions()
                .forEach(s -> sessionUpdateHandler.save(s, roleNodeIdMap.get(CONTROL_ACTIVITY_ROLE).get(0)));
    }

    private void delete(ControlActivityDTO controlActivityDTO) {
        queryService.deleteInitiatorByOuterId(controlActivityDTO.getId());
        controlActivityDTO.getSessions().forEach(sessionUpdateHandler::delete);
    }

    private void handleControlActivity(ControlActivityDTO response) {
        ControlActivityEntity activity = new ControlActivityEntity();
        activity.setOuterId(response.getId());
        activity.setDateCreated(response.getActivity().getDateCreated());
        activity.setDateModified(response.getActivity().getDateModified());
        setAuthor(activity, response.getActivity().getAuthorId());
        setMembers(activity, response.getActivity().getMembers());
        addRoleNodeId(CONTROL_ACTIVITY_ROLE, controlActivityRepository.save(activity).getId());
        addRoleNodeId(INITIATOR_ROLE, controlActivityRepository.save(activity).getId());
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

    public boolean needToUpdate(ChecklistDateDTO dto) {
        ControlActivityEntity activity = controlActivityRepository.findByOuterId(dto.getId());
        return isNull(activity) || dto.getDateModified().isAfter(activity.getDateModified());
    }
}

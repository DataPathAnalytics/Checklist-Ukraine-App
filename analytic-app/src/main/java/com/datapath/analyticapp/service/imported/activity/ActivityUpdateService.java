package com.datapath.analyticapp.service.imported.activity;

import com.datapath.analyticapp.dao.entity.imported.ControlActivityEntity;
import com.datapath.analyticapp.dao.repository.ControlActivityRepository;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dto.imported.response.ResponseDataDTO;
import com.datapath.analyticapp.dto.imported.response.SessionDTO;
import com.datapath.analyticapp.service.db.DatabaseUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Slf4j
public class ActivityUpdateService {

    private final ControlActivityRepository controlActivityRepository;
    private final UserRepository userRepository;
    private final DatabaseUtils dbUtils;

    @Transactional
    public void update(ResponseDataDTO response) {
        log.info("Updating control activity {}", response.getId());

        if (response.getActivity().isInvalid()) {
            //TODO:handle remove logic
        }

        Long controlActivityId = getControlActivityId(response);
        updateTypeQuestions(response.getActivity(), controlActivityId);
    }

    private void updateTypeQuestions(SessionDTO activity, Long controlActivityId) {
//        activity.get
    }

    private Long getControlActivityId(ResponseDataDTO response) {
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
}

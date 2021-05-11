package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.domain.ExportSessionResponseDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.entity.SessionEntity;
import com.datapath.checklistapp.dao.repository.ResponseSessionRepository;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import com.datapath.checklistapp.util.database.SessionPlace;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ResponseSessionDaoService {

    private static final String RESPONSE_SESSION_DATE_LIST_REQUEST = "select rs.id as id, rs.date_modified as dateModified from session rs " +
            "join control_activity ca on ca.id = rs.activity_id " +
            "where rs.status_id = 2 and " +
            "rs.place = ? and " +
            "rs.date_modified > ? " +
            "order by rs.date_modified limit ?";

    private final ResponseSessionRepository repository;
    private final JdbcTemplate template;

    public SessionEntity save(SessionEntity entity) {
        return repository.save(entity);
    }

    public SessionEntity findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Entity.ResponseSession.name(), id));
    }

    public SessionEntity findByIdAndActivity(Integer id, ControlActivityEntity activity) {
        return repository.findByIdAndActivity(id, activity)
                .orElseThrow(() -> new EntityNotFoundException(Entity.ResponseSession.name(), id));
    }

    public List<ExportSessionResponseDomain> getResponseSessionDates(LocalDateTime dateModified, int limit) {
        return template.query(RESPONSE_SESSION_DATE_LIST_REQUEST,
                new BeanPropertyRowMapper<>(ExportSessionResponseDomain.class),
                SessionPlace.SESSION_RESPONSE.name(), dateModified, limit);
    }

    public Page<SessionEntity> findResponseSessionByActivity(ControlActivityEntity activity, int page, int size) {
        return repository.findAllByActivityAndPlaceOrderByNumber(activity, SessionPlace.SESSION_RESPONSE, PageRequest.of(page, size));
    }
}

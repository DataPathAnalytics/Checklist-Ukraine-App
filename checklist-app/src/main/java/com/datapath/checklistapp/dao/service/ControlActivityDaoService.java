package com.datapath.checklistapp.dao.service;

import com.datapath.checklistapp.dao.domain.ExportControlActivityDomain;
import com.datapath.checklistapp.dao.entity.ControlActivityEntity;
import com.datapath.checklistapp.dao.repository.ControlActivityRepository;
import com.datapath.checklistapp.dto.request.page.PageableRequest;
import com.datapath.checklistapp.exception.EntityNotFoundException;
import com.datapath.checklistapp.util.database.Entity;
import com.datapath.checklistapp.util.database.ResponseRole;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class ControlActivityDaoService {

    private final static String CONTROL_ACTIVITY_DATE_LIST_REQUEST = "select ca.id as id, ars.date_modified as dateModified " +
            "from control_activity ca " +
            "join session ars on ca.id = ars.activity_id " +
            "where ars.date_modified > ?::timestamp at time zone 'Etc/UTC' and " +
            "ars.role = ? and " +
            "exists (select id from session where activity_id = ca.id and status_id = 2 and role = ?) " +
            "order by ars.date_modified limit ?";

    private final ControlActivityRepository repository;
    private final JdbcTemplate template;

    public Page<ControlActivityEntity> findAll(Integer userId, PageableRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());

        if (nonNull(userId))
            return repository.findUserControlActivities(userId, ResponseRole.ACTIVITY_RESPONSE.name(), pageRequest);
        return repository.findAll(ResponseRole.ACTIVITY_RESPONSE.name(), pageRequest);
    }

    public ControlActivityEntity save(ControlActivityEntity entity) {
        return repository.saveAndFlush(entity);
    }

    public ControlActivityEntity findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Entity.ControlActivity.name(), id));
    }

    public List<ExportControlActivityDomain> getControlActivityDates(LocalDateTime offset, int limit) {
        return template.query(CONTROL_ACTIVITY_DATE_LIST_REQUEST,
                new BeanPropertyRowMapper<>(ExportControlActivityDomain.class),
                offset, ResponseRole.ACTIVITY_RESPONSE.name(), ResponseRole.SESSION_RESPONSE.name(), limit);
    }


}

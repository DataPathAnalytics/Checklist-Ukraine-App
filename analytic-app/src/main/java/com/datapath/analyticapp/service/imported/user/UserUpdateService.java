package com.datapath.analyticapp.service.imported.user;

import com.datapath.analyticapp.dao.entity.imported.DepartmentEntity;
import com.datapath.analyticapp.dao.entity.imported.EmploymentEntity;
import com.datapath.analyticapp.dao.entity.imported.UserEntity;
import com.datapath.analyticapp.dao.repository.DepartmentRepository;
import com.datapath.analyticapp.dao.repository.UserRepository;
import com.datapath.analyticapp.dto.imported.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserUpdateService {

    private final UserRepository repository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void process(UserDTO dto) {
        UserEntity entity = repository.findFirstByEmail(dto.getEmail()).orElseGet(UserEntity::new);

        entity.setOuterId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateModified(dto.getDateModified());

        if (isNull(entity.getId())) {
            entity.setRegisteredDate(dto.getRegisteredDate());
            entity.setExportDate(LocalDateTime.now());
        }

        List<EmploymentEntity> employment = dto.getEmployments()
                .stream()
                .map(eDto -> {
                    EmploymentEntity employmentEntity = new EmploymentEntity();
                    employmentEntity.setStart(eDto.getStart());
                    employmentEntity.setEnd(eDto.getEnd());
                    employmentEntity.setDepartment(
                            departmentRepository.findFirstByOuterId(eDto.getDepartment().getIdentifier())
                                    .orElseGet(() -> new DepartmentEntity(eDto.getDepartment().getIdentifier(), eDto.getDepartment().getName()))
                    );
                    return employmentEntity;
                }).collect(toList());

        entity.setEmployments(employment);

        repository.save(entity);
    }
}

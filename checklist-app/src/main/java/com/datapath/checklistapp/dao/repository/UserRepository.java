package com.datapath.checklistapp.dao.repository;

import com.datapath.checklistapp.dao.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    String ADMINS_QUERY = "select * from users u " +
            "join user_permission up on u.id = up.user_id " +
            "where up.permission_id = 1 and u.super_admin is false";

    String NOT_CHECKED_USER_QUERY = "select exists (select id from users where locked = true)";

    UserEntity findByEmail(String email);

    UserEntity findByEmailAndRemovedIsFalse(String email);

    @Query(value = ADMINS_QUERY, nativeQuery = true)
    List<UserEntity> findAdmins();

    @Query(value = NOT_CHECKED_USER_QUERY, nativeQuery = true)
    boolean existsNotChecked();

    Page<UserEntity> findAllByRemovedIsFalseAndSuperAdminIsFalseOrderByLockedDescRegisteredDateDescFirstNameAsc(Pageable pageable);

    List<UserEntity> findAllByDateModifiedIsNotNullAndDateModifiedAfterOrderByDateModified(LocalDateTime date, PageRequest of);
}

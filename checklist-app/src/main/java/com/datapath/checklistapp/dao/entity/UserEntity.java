package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.dao.entity.classifier.Permission;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Table
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private boolean disable;
    private boolean locked;
    private boolean removed;
    private boolean superAdmin;
    @CreationTimestamp
    private LocalDateTime registeredDate;
    @UpdateTimestamp
    private LocalDateTime dateModified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_permission",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "permission_id"}))
    private Set<Permission> permissions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<EmploymentEntity> employments = new HashSet<>();
}

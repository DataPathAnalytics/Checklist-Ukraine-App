package com.datapath.checklistapp.dao.entity;

import com.datapath.checklistapp.util.converter.ComponentType;
import com.datapath.checklistapp.util.converter.ValueType;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "field_description")
public class FieldDescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String label;
    @Enumerated(EnumType.STRING)
    private ValueType valueType;
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;
    private boolean title;
    private boolean required;
    private boolean identifier;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "field_description_id")
    private Set<ValueEntity> values = new HashSet<>();
}

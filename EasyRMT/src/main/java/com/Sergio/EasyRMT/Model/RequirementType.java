/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import com.Sergio.EasyRMT.Model.types.Requirement_Type;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requirement_type")
@EqualsAndHashCode
@Getter
@Setter
public class RequirementType implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtype")
    @Basic(optional = false)
    private int idType;

    @NotNull
    @Length(min=1, max = 64)
    @Column(name = "requirement_name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "requirement_type")
    private Requirement_Type type;

    @JsonBackReference
    @ManyToMany(mappedBy = "requirementTypes", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

}

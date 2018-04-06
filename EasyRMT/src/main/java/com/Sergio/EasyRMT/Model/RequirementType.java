/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

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
public class RequirementType implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtype")
    @Basic(optional = false)
    @Getter
    @Setter
    private int idType;

    @NotNull
    @Length(min=1, max = 64)
    @Setter
    @Getter
    @Column(name = "type")
    private String name;

    @Getter
    @Setter
    @JsonBackReference
    @ManyToMany(mappedBy = "requirementTypes", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

}

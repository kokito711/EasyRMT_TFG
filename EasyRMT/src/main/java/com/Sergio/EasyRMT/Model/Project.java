
/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import com.Sergio.EasyRMT.Model.types.ProjectType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproject")
    private int idProject;

    @NotNull
    @Length(min=1, max = 64)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "projecttype")
    private ProjectType type;

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "project_has_requirement_type",
        joinColumns = @JoinColumn(name = "project_idproject", referencedColumnName = "idproject", foreignKey = @ForeignKey(name = "idproject")),
        inverseJoinColumns = @JoinColumn(name = "object_types_idtype", referencedColumnName = "idtype", foreignKey = @ForeignKey(name = "idtype")))
    private List<RequirementType> requirementTypes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "group_groupId", referencedColumnName = "group_id")
    private Group group;

}

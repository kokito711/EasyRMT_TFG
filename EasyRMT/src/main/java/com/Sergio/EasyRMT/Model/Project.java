
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
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproject")
    @Getter
    @Setter
    private int idProject;

    @NotNull
    @Length(min=1, max = 64)
    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Lob
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "projecttype")
    private ProjectType type;

    @Getter
    @Setter
    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "project_has_requirement_type",
        joinColumns = @JoinColumn(name = "project_idproject", referencedColumnName = "idproject", foreignKey = @ForeignKey(name = "idproject")),
        inverseJoinColumns = @JoinColumn(name = "object_types_idtype", referencedColumnName = "idtype", foreignKey = @ForeignKey(name = "idtype")))
    private List<RequirementType> requirementTypes = new ArrayList<>();

}

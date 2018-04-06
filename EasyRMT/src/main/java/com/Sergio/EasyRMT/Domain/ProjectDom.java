
/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Domain;


import com.Sergio.EasyRMT.Model.types.ProjectType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ProjectDom implements Serializable {

    @Getter
    @Setter
    private int idProject;

    @Setter
    @Getter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private ProjectType type;

    @Getter
    @Setter
    private List<RequirementTypeDom> requirementTypes;

    public ProjectDom(int idProject, String name, String description, ProjectType type, List<RequirementTypeDom> requirementTypes) {
        this.idProject = idProject;
        this.name = name;
        this.description = description;
        this.type = type;
        this.requirementTypes = requirementTypes;
    }

    public ProjectDom(int idProject, String name) {
        this.idProject = idProject;
        this.name = name;
    }

    public ProjectDom(int idProject) {
        this.idProject = idProject;
    }
}

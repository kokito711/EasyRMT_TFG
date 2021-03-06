
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

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
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

    @Getter
    @Setter
    private GroupDom group;

    @Getter
    @Setter
    private  List<String> stringReqTypes;

    @Getter
    @Setter
    private int groupId;

    public ProjectDom(int idProject, String name, String description, ProjectType type, List<RequirementTypeDom> requirementTypes, GroupDom group) {
        this.idProject = idProject;
        this.name = name;
        this.description = description;
        this.type = type;
        this.requirementTypes = requirementTypes;
        this.group = group;
    }

    public ProjectDom(int idProject, String name) {
        this.idProject = idProject;
        this.name = name;
    }

    public ProjectDom(int idProject) {
        this.idProject = idProject;
    }

    public ProjectDom() {
        requirementTypes = new ArrayList<>();
        stringReqTypes = new ArrayList<>();
    }
}

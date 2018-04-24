/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.types.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@EqualsAndHashCode
public class RequirementDom implements Serializable {

    @Getter
    @Setter
    private int idRequirement;

    @Getter
    @Setter
    private String name;

    @Setter
    @Getter
    private String identifier;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Priority priority;

    @Getter
    @Setter
    private Complexity complexity;

    @Getter
    @Setter
    private State state;

    @Getter
    @Setter
    private Double cost;

    @Getter
    @Setter
    private Double estimatedHours;

    @Getter
    @Setter
    private Integer storyPoints;

    @Setter
    @Getter
    private String source;

    @Getter
    @Setter
    private Scope scope;

    @Getter
    @Setter
    private Risk risk;

    @Getter
    @Setter
    private Date created;

    @Getter
    @Setter
    private Date lastUpdated;

    @Setter
    @Getter
    private String version;

    @Getter
    @Setter
    private String validationMethod;

    @Getter
    @Setter
    private Integer author;

    @Getter
    @Setter
    private Integer assignedTo;

    @Getter
    @Setter
    private String justification;

    @Getter
    @Setter
    private String testCases;

    @Getter
    @Setter
    private Integer requirementTypeId;

    @Getter
    @Setter
    private Integer projectId;

    public RequirementDom() {
    }

    public RequirementDom(int idRequirement, String name, String identifier, Integer author, Integer assignedTo,
                          Integer requirementType, Integer projectId) {
        this.idRequirement = idRequirement;
        this.name = name;
        this.identifier = identifier;
        this.author = author;
        this.assignedTo = assignedTo;
        this.requirementTypeId = requirementType;
        this.projectId = projectId;
    }

    public RequirementDom(int idRequirement, String name, String identifier, String description, Priority priority,
                          Complexity complexity, State state, Double cost, Double estimatedHours, Integer storyPoints,
                          String source, Scope scope, Risk risk, Date created, Date lastUpdated, String version,
                          String validationMethod, Integer author, Integer assignedTo, String justification,
                          String testCases, Integer requirementTypeId, Integer projectId) {
        this.idRequirement = idRequirement;
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.priority = priority;
        this.complexity = complexity;
        this.state = state;
        this.cost = cost;
        this.estimatedHours = estimatedHours;
        this.storyPoints = storyPoints;
        this.source = source;
        this.scope = scope;
        this.risk = risk;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.version = version;
        this.validationMethod = validationMethod;
        this.author = author;
        this.assignedTo = assignedTo;
        this.justification = justification;
        this.testCases = testCases;
        this.requirementTypeId = requirementTypeId;
        this.projectId = projectId;
    }
}

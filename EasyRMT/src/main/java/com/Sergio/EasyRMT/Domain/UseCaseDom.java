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

import java.util.Date;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UseCaseDom {

    private int idUseCase;

    private String name;

    private String identifier;

    private String description;

    private Priority priority;

    private Complexity complexity;

    private State state;

    private Double cost;

    private Double estimatedHours;

    private Integer storyPoints;

    private String source;

    private Scope scope;

    private Risk risk;

    private Date created;

    private Date lastUpdated;

    private String version;

    private String validationMethod;

    private UserDom author;

    private UserDom assignedTo;

    private String justification;

    private String testCases;

    private String actors;

    private String preconditions;

    private String postconditions;

    private String steps;

    private int projectId;
    private int featureId;
    private int authorId;
    private int assignedId;

    public UseCaseDom() {
    }

    public UseCaseDom(int idUseCase, String name, String identifier, UserDom author, UserDom assignedTo,
                      int projectId, int featureId) {
        this.idUseCase = idUseCase;
        this.name = name;
        this.identifier = identifier;
        this.author = author;
        this.assignedTo = assignedTo;
        this.projectId = projectId;
        this.featureId = featureId;
    }

    public UseCaseDom(int idUseCase, String name, String identifier, String description, Priority priority,
                      Complexity complexity, State state, Double cost, Double estimatedHours, Integer storyPoints,
                      String source, Scope scope, Risk risk, Date created, Date lastUpdated, String version,
                      String validationMethod, UserDom author, UserDom assignedTo, String justification,
                      String testCases, String actors, String preconditions, String postconditions,
                      String steps, int projectId, int featureId) {
        this.idUseCase = idUseCase;
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
        this.actors = actors;
        this.preconditions = preconditions;
        this.postconditions = postconditions;
        this.steps = steps;
        this.projectId = projectId;
        this.featureId = featureId;
    }
}

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
public class UserStoryDom {

    @Getter
    @Setter
    private int idUserStory;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String identifier;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String definitionOfDone;

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
    private double cost;

    @Getter
    @Setter
    private double estimatedHours;

    @Getter
    @Setter
    private int storyPoints;

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
    private int author;

    @Getter
    @Setter
    private int assignedTo;

    @Getter
    @Setter
    private String justification;

    @Getter
    @Setter
    private String testCases;


    @Getter
    @Setter
    private int projectId;

    @Getter
    @Setter
    private int epicId;

    public UserStoryDom() {
    }

    public UserStoryDom(int idUserStory, String name, String identifier, int author, int assignedTo,  int projectId,
                        int epicId) {
        this.idUserStory = idUserStory;
        this.name = name;
        this.identifier = identifier;
        this.author = author;
        this.assignedTo = assignedTo;
        this.projectId = projectId;
        this.epicId = epicId;
    }

    public UserStoryDom(int idUserStory, String name, String identifier, String description, String definitionOfDone,
                        Priority priority, Complexity complexity, State state, double cost, double estimatedHours,
                        int storyPoints, String source, Scope scope, Risk risk, Date created, Date lastUpdated,
                        String version, String validationMethod, int author, int assignedTo, String justification,
                        String testCases, int projectId, int epicId) {
        this.idUserStory = idUserStory;
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.definitionOfDone = definitionOfDone;
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
        this.projectId = projectId;
        this.epicId = epicId;
    }
}
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
public class UserStoryDom {

    private int idUserStory;

    private String name;

    private String identifier;

    private String description;

    private String definitionOfDone;

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

    private int projectId;
    private int epicId;
    private int authorId;
    private int assignedId;

    public UserStoryDom() {
    }

    public UserStoryDom(int idUserStory, String name, String identifier, UserDom author, UserDom assignedTo,  int projectId,
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
                        String version, String validationMethod, UserDom author, UserDom assignedTo, String justification,
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

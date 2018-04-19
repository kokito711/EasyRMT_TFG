/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import com.Sergio.EasyRMT.Model.types.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Epic")
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Epic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idobject")
    @Getter
    @Setter
    private int idEpic;

    @NotNull
    @Length(min=1, max = 64)
    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Length( max = 10)
    @Setter
    @Getter
    @Column(name = "identifier")
    private String identifier;

    @Getter
    @Setter
    @Lob
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @Lob
    @Column(name = "definitionofdone")
    private String definitionOfDone;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private Complexity complexity;

    @Getter
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Getter
    @Setter
    @NotNull
    @Column(name = "cost", columnDefinition="Decimal(10,2) default '0.00'")
    private Double cost;

    @Getter
    @Setter
    @NotNull
    @Column(name = "estimatedhours", columnDefinition="Decimal(4,2) default '0.00'")
    private Double estimatedHours;

    @Getter
    @Setter
    @Column(name="storypoints")
    private Integer storyPoints;

    @Length(min=1, max = 64)
    @Setter
    @Getter
    @Column(name = "source")
    private String source;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private Scope scope;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "risk")
    private Risk risk;

    @Getter
    @Setter
    @NotNull
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @Getter
    @Setter
    @NotNull
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date lastUpdated;

    @Length(min=1, max = 45)
    @Setter
    @Getter
    @Column(name = "version")
    private String version;

    @Getter
    @Setter
    @Lob
    @Column(name = "validationmethod")
    private String validationMethod;

    @Getter
    @Setter
    @NotNull
    @Column(name="author")
    private Integer author;

    @Getter
    @Setter
    @Column(name="assignedto")
    private Integer assignedTo;

    @Getter
    @Setter
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "justification")
    private String justification;

    @Getter
    @Setter
    @Lob
    @Column(name = "testcases")
    private String testCases;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "object_idobject")
    @NotNull
    @Getter
    @Setter
    private ObjectEntity object;

    @Getter
    @Setter
    @OneToMany(mappedBy = "epic")
    private List<UserStory> userStories;
}

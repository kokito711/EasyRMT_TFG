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
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_story")
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class UserStory implements Serializable {
   private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idobject")
    @Basic(optional = false)
    private int idUserStory;

    @NotNull
    @Length(min=1, max = 64)
    @Column(name = "name")
    private String name;

    @Length(min=1, max = 10)
    @Column(name = "identifier")
    private String identifier;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "definitionofdone")
    private String definitionOfDone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private Complexity complexity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @NotNull
    @Column(name = "cost", columnDefinition="Decimal(10,2) default '0.00'")
    private Double cost = 0.00;

    @NotNull
    @Column(name = "estimatedhours", columnDefinition="Decimal(4,2) default '0.00'")
    private Double estimatedHours = 0.00;

    @Column(name="storypoints")
    private Integer storyPoints;

    @Length(max = 64)
    @Column(name = "source")
    private String source;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private Scope scope;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk")
    private Risk risk;

    @NotNull
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @NotNull
    @Column(name = "lastupdated")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date lastUpdated;

    @Length(max = 45)
    @Column(name = "version")
    private String version;

    @Lob
    @Column(name = "validationmethod")
    private String validationMethod;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignedto", referencedColumnName = "user_id")
    private User assignedTo;

    @Lob
    @Size(max = 16777215)
    @Column(name = "justification")
    private String justification;

    @Lob
    @Column(name = "testcases")
    private String testCases;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "object_idobject")
    @NotNull
    private ObjectEntity object;

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "epic", referencedColumnName = "idobject")
    @NotNull
    private Epic epic;
}

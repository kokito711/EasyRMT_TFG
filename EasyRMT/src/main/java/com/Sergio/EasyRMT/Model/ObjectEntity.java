/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "object")
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class ObjectEntity implements Serializable{
   private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idobject")
    private int idobject;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_idproject", referencedColumnName = "idproject")
    @NotNull
    private Project project;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "Traceability",
         joinColumns = @JoinColumn(name = "Object1", referencedColumnName = "idobject"),
         inverseJoinColumns = @JoinColumn(name = "Object2", referencedColumnName = "idobject")
    )
    private List<ObjectEntity> traced = new ArrayList<>();
}

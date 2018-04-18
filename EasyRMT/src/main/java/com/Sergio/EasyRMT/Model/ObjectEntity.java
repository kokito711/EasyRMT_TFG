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

@Entity
@Table(name = "object")
@ToString
@EqualsAndHashCode
public class ObjectEntity implements Serializable{
   private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idobject")
    @Getter
    @Setter
    private int idobject;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_idproject", referencedColumnName = "idproject")
    @NotNull
    @Getter
    @Setter
    private Project project;

}

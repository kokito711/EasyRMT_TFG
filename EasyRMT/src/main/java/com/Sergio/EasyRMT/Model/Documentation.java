/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "documentation")
@EqualsAndHashCode
@Getter
@Setter
public class Documentation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "iddocumentation")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDocumentation;

    @NotNull
    @Column(name = "name")
    @Length(max = 64, min = 1)
    private String name = "file";

    @NotNull
    @Column(name = "size")
    private Double size = 0.0;

    @NotNull
    @Column(name = "type")
    @Length(max = 255, min = 1)
    private String type = "application/octet-stream";

    @NotNull
    @Column(name = "path")
    @Length(max = 255, min = 1)
    private String path = "/";

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name="idobject", referencedColumnName = "idobject")
    private ObjectEntity object;

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idproject", referencedColumnName = "idproject")
    @NotNull
    private Project project;
}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */
package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.ObjectEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DocumentationDom implements Serializable{
    private int idDocumentation;

    @NotNull
    @Builder.Default
    @Length(max = 64, min = 1)
    private String name = "file";

    @NotNull
    @Builder.Default
    private Double size = 0.0;

    @NotNull
    @Builder.Default
    @Length(max = 255, min = 1)
    private String type = "application/octet-stream";

    @NotNull
    @Builder.Default
    @Length(max = 255, min = 1)
    private String path = "/";

    private ObjectEntity object;

    @NotNull
    private ProjectDom project;

    public DocumentationDom() {
        this.project = new ProjectDom();
    }

    public DocumentationDom(int idDocumentation, String name, Double size, String type, String path,
                            ObjectEntity object, ProjectDom project) {
        this.idDocumentation = idDocumentation;
        this.name = name;
        this.size = size;
        this.type = type;
        this.path = path;
        this.object = object;
        this.project = project;
    }
}

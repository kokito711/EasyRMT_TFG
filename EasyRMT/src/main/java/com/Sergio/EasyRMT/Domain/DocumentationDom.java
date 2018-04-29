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
    }
}

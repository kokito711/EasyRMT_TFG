/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.types.Requirement_Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
public class RequirementTypeDom implements Serializable{


    private int idType;

    private String name;

    private Requirement_Type type;


    public RequirementTypeDom(int idType, String name, Requirement_Type type) {
        this.idType = idType;
        this.name = name;
        this.type = type;
    }

    public RequirementTypeDom(int idType) {
        this.idType = idType;
    }
}

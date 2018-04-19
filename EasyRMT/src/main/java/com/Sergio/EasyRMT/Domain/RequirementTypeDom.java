/*
 * Copyright (c) 2018. Sergio López Jiménez and Universidad de Valladolid
 * All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
public class RequirementTypeDom implements Serializable{

    @Getter
    @Setter
    private int idType;

    @Setter
    @Getter
    private String name;

    public RequirementTypeDom(int idType, String name) {
        this.idType = idType;
        this.name = name;
    }

    public RequirementTypeDom(int idType) {
        this.idType = idType;
    }
}

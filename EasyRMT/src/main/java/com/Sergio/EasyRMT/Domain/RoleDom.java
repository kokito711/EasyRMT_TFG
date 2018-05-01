/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class RoleDom {

    private int roleId;

    @NotNull
    @NotEmpty
    private String role;
}

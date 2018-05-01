/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class UserDom {

    private int userId;

    @NotNull
    @Length(min=1, max = 16)
    @NotEmpty
    private String username;

    @NotNull
    @Length(min=1, max = 255)
    @Email
    @NotEmpty
    private String email;

    @NotNull
    @Length(min=1, max = 150)
    @NotEmpty
    private String password;

    @Length(min=1, max = 30)
    private String name;

    @Length(min=1, max = 40)
    private String lastName;

    @Length(min=1, max = 15)
    private String phone;
}

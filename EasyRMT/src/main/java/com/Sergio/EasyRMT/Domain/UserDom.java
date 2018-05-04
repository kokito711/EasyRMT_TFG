/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.Group_user;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode
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
    @Transient
    private String password;

    @Length(max = 30)
    private String name;

    @Length(max = 40)
    private String lastName;

    @Length(max = 15)
    private String phone;

    private List<RoleDom> roles;

    private List<Group_user> groups;

    private List<String> stringRoles;

    public UserDom() {
    }

    public UserDom(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDom(int userId, String username, String email, String password, String name, String lastName, String phone, List<RoleDom> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.roles = roles;
    }
}

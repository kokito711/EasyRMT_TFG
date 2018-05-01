/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
@EqualsAndHashCode
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @NotNull
    @Length(min=1, max = 16)
    @Column(name = "username")
    private String username;

    @NotNull
    @Length(min=1, max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Length(min=1, max = 150)
    @Column(name = "password")
    private String password;

    @Length(min=1, max = 30)
    @Column(name = "name")
    private String name;

    @Length(min=1, max = 40)
    @Column(name = "lastname")
    private String lastName;

    @Length(min=1, max = 15)
    @Column(name = "phone")
    private String phone;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}

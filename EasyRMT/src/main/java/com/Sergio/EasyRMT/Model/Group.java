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
import java.util.List;

@Entity
@Table(name = "app_group", schema = "easyrmt")
@EqualsAndHashCode
@Getter
@Setter
public class Group implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int group_id;

    @NotNull
    @Length(min=1, max = 45)
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "primaryKey.group", cascade = CascadeType.MERGE)
    List<Group_user> group;
}

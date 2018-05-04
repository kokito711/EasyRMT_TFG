/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.Group_user;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@Setter
@Getter
public class GroupDom {

    private int groupId;

    private String name;

    private List<Group_user> users;

    private List<String> stringUsers;
    private List<String> stakeholders;

    private int pm;

    public GroupDom() {
    }

    public GroupDom(int groupId, String name, List<Group_user> users) {
        this.groupId = groupId;
        this.name = name;
        this.users = users;
    }
}

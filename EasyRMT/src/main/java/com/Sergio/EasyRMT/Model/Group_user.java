/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_user")
@EqualsAndHashCode
@Getter
@Setter
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user", joinColumns = @JoinColumn(name = "user_id")),
        @AssociationOverride(name = "primaryKey.group", joinColumns = @JoinColumn(name="group_id"))
})
public class Group_user implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private Group_UserKey primaryKey;


    @NotNull
    @Column(name = "isPM")
    private boolean isPM = false;

    @NotNull
    @Column(name = "isStakeholder")
    private boolean isStakeholder = false;

    public Group_user(User user, Group group, boolean isPM, boolean isStakeholder) {
        this.primaryKey = new Group_UserKey(user, group);
        this.isPM = isPM;
        this.isStakeholder = isStakeholder;
        if(primaryKey.getUser().getGroups() == null){
            List<Group_user> list = new ArrayList<>();
            primaryKey.getUser().setGroups(list);
        }
        if(primaryKey.getGroup().getGroup() == null){
            List<Group_user> list = new ArrayList<>();
            primaryKey.getGroup().setGroup(list);
        }
        primaryKey.getUser().getGroups().add(this);
        primaryKey.getGroup().getGroup().add(this);
    }

    public Group_user() {
    }
}

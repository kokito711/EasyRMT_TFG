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

}

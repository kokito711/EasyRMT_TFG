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
import java.util.Date;

@Entity
@Table(name = "comment")
@Getter
@Setter
@EqualsAndHashCode
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idComment")
    private int idComment;

    @NotNull
    @Lob
    @Length(min=1)
    @Column(name = "comment")
    private String comment;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified")
    private Date lastModified;

    @NotNull
    @JoinColumn(name = "objectId", referencedColumnName = "idobject")
    private ObjectEntity object;

    @NotNull
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private User user;
}

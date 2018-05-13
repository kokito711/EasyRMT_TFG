/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Domain;

import com.Sergio.EasyRMT.Model.ObjectEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CommentDom {

    private int idComment;

    private String comment;

    private Date created;

    private Date lastModified;

    private ObjectEntity object = new ObjectEntity();

    private UserDom user = new UserDom();

    public CommentDom() {
    }

    public CommentDom(int idComment, String comment, Date created, Date lastModified, ObjectEntity object, UserDom user) {
        this.idComment = idComment;
        this.comment = comment;
        this.created = created;
        this.lastModified = lastModified;
        this.object = object;
        this.user = user;
    }
}

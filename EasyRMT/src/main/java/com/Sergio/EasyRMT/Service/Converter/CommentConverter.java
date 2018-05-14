/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Comment;
import com.Sergio.EasyRMT.Model.Epic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentConverter {
    private UserConverter userConverter;

    @Autowired
    public CommentConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    /**
     * this method converts a list of comments (Model) to a list of comments(Domain)
     * @param comments List of {@link Comment} obtained from DB
     * @return List of {@link CommentDom}
     */
    public List<CommentDom> toDomain(List<Comment> comments) {
        List<CommentDom> commentDomList = new ArrayList<>();
        for(Comment comment : comments){
            CommentDom commentDom = toDomain(comment);
            commentDomList.add(commentDom);
        }
        return commentDomList;
    }

    /**
     * This method converts a {@link Comment} (DB) to a {@link CommentDom} (Domain)
     * @param comment {@link Comment} obtained from DB
     * @return {@link CommentDom}
     */
    public CommentDom toDomain(Comment comment){
        UserDom user = userConverter.toDomain(comment.getUser());

        CommentDom commentDom = new CommentDom(
                comment.getIdComment(),
                comment.getComment(),
                comment.getCreated(),
                comment.getLastModified(),
                comment.getObject(),
                user
        );
        return commentDom;
    }

    /**
     * This method converts a {@link CommentDom} (Domain) to a {@link Comment} (DB)
     * @param commentDom {@link CommentDom}
     * @return {@link Comment}
     */
    public Comment toModel(CommentDom commentDom){
        Comment comment = new Comment();
        comment.setComment(commentDom.getComment());
        comment.setCreated(commentDom.getCreated());
        comment.setLastModified(commentDom.getLastModified());
        return comment;
    }
}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Model.Comment;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.CommentRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.CommentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    UserRepository userRepository;
    ObjectRepository objectRepository;
    CommentConverter commentConverter;
    CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository, ObjectRepository objectRepository, CommentConverter commentConverter,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.objectRepository = objectRepository;
        this.commentConverter = commentConverter;
        this.commentRepository = commentRepository;
    }

    /**
     * This method returns a list of comments existing in one object
     * @param objectId object that have comments
     * @return {@link List<CommentDom>} list with all comments sorted by creation date desc.
     */
    @Transactional(readOnly = true)
    public List<CommentDom> getComments (int objectId){
        List<Comment> comments = commentRepository.findByObjectId(objectId);
        List<CommentDom> commentDomList = commentConverter.toDomain(comments);
        return commentDomList;
    }

    /**
     * This method creates a new comment and make a relationship with the creator and the object in which will be attached
     * @param commentDom new comment to be persisted
     * @return True if comment has been saved or false if it does not.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createComment(CommentDom commentDom){
        Date timestamp = new Date();
        commentDom.setCreated(timestamp);
        commentDom.setLastModified(timestamp);
        Comment comment = commentConverter.toModel(commentDom);
        User user = userRepository.findOne(commentDom.getUser().getUserId());
        comment.setUser(user);
        ObjectEntity objectEntity = objectRepository.findOne(commentDom.getObject().getIdobject());
        comment.setObject(objectEntity);

        comment = commentRepository.save(comment);
        if(comment==null){
            return false;
        }
        return true;
    }

    /**
     * This method updates an existing comment.
     * @param commentDom comment to be updated
     * @return True if comment has been updated or false if it does not.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateComment(CommentDom commentDom){
        Date timestamp = new Date();
        Comment comment = commentRepository.findOne(commentDom.getIdComment());
        comment.setLastModified(timestamp);
        comment.setComment(commentDom.getComment());
        comment = commentRepository.save(comment);
        if(comment==null){
            return false;
        }
        return true;
    }

    /**
     * This method will delete an existing comment.
     * @param commentId id of comment to be deleted
     * @return True if comment has been deleted or false if it does not.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(int commentId){
        if(commentRepository.exists(commentId)){
            commentRepository.delete(commentId);
            if(commentRepository.exists(commentId)){
                return false;
            }
            return true;
        }
        return false;
    }
}

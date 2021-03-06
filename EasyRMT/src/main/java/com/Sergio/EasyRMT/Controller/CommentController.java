/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Service.CommentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CommentController {
    private CommentService commentService;
    private CommonMethods commonMethods;
    private UserService userService;
    private ProjectService projectService;
    private final String PATH_BASE = "/{projectId}/{objectId}/";
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final String loggerMessage = "Unauthorized attempt to access: ";
    @Autowired
    public CommentController(CommentService commentService, CommonMethods commonMethods, UserService userService,
                             ProjectService projectService) {
        this.commentService = commentService;
        this.commonMethods = commonMethods;
        this.userService = userService;
        this.projectService = projectService;
    }

    /**
     * Exception handler which is thrown when any exception occurs
     * @return BAD REQUEST response
     */
    @ExceptionHandler(value ={Exception.class})
    protected ResponseEntity handle(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    /**
     * This method adds a comment into one object.
     * @param projectId id of project in which is attached the object
     * @param objectId id of object in which will be attached the comment
     * @param comment Mesasge to be persisted
     * @param principal Session info
     * @return {@link ResponseEntity} 200,OK if comment has been saved, 400,BAD_REQUEST if comment has not.
     * @throws AccessDeniedException if user has no rights to delete a comment
     */
    @RequestMapping(value = PATH_BASE+"addComment", method = RequestMethod.POST)
    public ResponseEntity addComment(@PathVariable int projectId, @PathVariable int objectId, @ModelAttribute CommentDom comment,
                                     Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            comment.setUser(user);
            ObjectEntity objectEntity = new ObjectEntity();
            objectEntity.setIdobject(objectId);
            comment.setObject(objectEntity);
            boolean created = commentService.createComment(comment);
            if (!created){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
            }
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to create a comment in object "+objectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This method updates an existing comment
     * @param projectId id of project in which is attached the object
     * @param objectId id of object in which will be attached the comment
     * @param comment Mesasge to be persisted
     * @param principal Session info
     * @param commentId id of comment to be updated
     * @return {@link ResponseEntity} 200,OK if comment has been updated, 400,BAD_REQUEST if comment has not.
     * @throws AccessDeniedException if user has no rights to delete a comment
     */
    @RequestMapping(value = PATH_BASE+"update/{commentId}", method = RequestMethod.POST)
    public ResponseEntity updateComment(@PathVariable int projectId, @PathVariable int objectId, @ModelAttribute CommentDom comment,
                                     @PathVariable int commentId, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            comment.setIdComment(commentId);
            boolean updated = commentService.updateComment(comment, user);
            if (!updated){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
            }
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to update a comment in object "+objectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This method deletes an existing comment.
     * @param projectId id of project in which is attached the object
     * @param objectId id of object in which will be attached the comment
     * @param principal Session info
     * @param commentId id of comment to be updated
     * @return {@link ResponseEntity} 200,OK if comment has been deleted, 400,BAD_REQUEST if comment has not.
     * @throws AccessDeniedException if user has no rights to delete a comment
     */
    @RequestMapping(value = PATH_BASE+"delete/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteComment(@PathVariable int projectId, @PathVariable int objectId,
                                        @PathVariable int commentId, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean updated = commentService.deleteComment(commentId, user);
            if (!updated){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
            }
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to delete a comment in object "+objectId);
        throw new AccessDeniedException("Not allowed");
    }

}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.TraceabilityService;
import com.Sergio.EasyRMT.Service.UserService;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class TraceabilityController {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final String loggerMessage = "Unauthorized attempt to access: ";
    private final String PATH_BASE = "/traceability/";
    private TraceabilityService traceabilityService;
    private UserService userService;
    private ProjectService projectService;
    private CommonMethods commonMethods;

    @Autowired
    public TraceabilityController(TraceabilityService traceabilityService, UserService userService,
                                  ProjectService projectService, CommonMethods commonMethods) {
        this.traceabilityService = traceabilityService;
        this.userService = userService;
        this.projectService = projectService;
        this.commonMethods = commonMethods;
    }

    @RequestMapping(value = PATH_BASE+"{projectId}/obj1/{obj1Id}/obj2/{obj2Id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRelationship(@PathVariable int projectId, @PathVariable int obj1Id, @PathVariable int obj2Id,
                                             Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean deleted = traceabilityService.deleteRelatonship(obj1Id,obj2Id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.OK).body("");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to delete a traceability " +
                "relationship from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }


    @RequestMapping(value =PATH_BASE+"{projectId}/obj1/{obj1Id}/obj2/{obj2Id}", method = RequestMethod.POST)
    public ResponseEntity createRelationship(@PathVariable int projectId, @PathVariable int obj1Id, @PathVariable int obj2Id,
                                             Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean created = traceabilityService.saveRelationship(obj1Id,obj2Id);
            if (created) {
                return ResponseEntity.status(HttpStatus.OK).body("");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to delete a traceability " +
                "relationship from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }



}

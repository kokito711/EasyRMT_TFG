
/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ProjectController {

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final String loggerMessage = "Unauthorized attempt to access: ";
    ProjectService projectService;
    DocumentService documentService;
    CommonMethods commonMethods;
    UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, DocumentService documentService, CommonMethods commonMethods, UserService userService) {
        this.projectService = projectService;
        this.documentService = documentService;
        this.commonMethods = commonMethods;
        this.userService = userService;
    }

    /**
     * This method returns the create project view
     * @param modelAndView with project object and an array with requirement types
     * @return ModelAndView which is model received as param
     */
    @RequestMapping(value="/createProject", method = RequestMethod.GET)
    public ModelAndView createProjectView(ModelAndView modelAndView, Principal principal){ ;
        ProjectDom projectDom = new ProjectDom();
        modelAndView.setViewName("createProject");
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("reqTypes", projectService.getReqTypes());
        modelAndView.addObject("user", principal.getName());
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        boolean isPm = commonMethods.isPM(user,principal.getName());
        List<Group_user> groups = user.getGroups();
        modelAndView.addObject("groups", groups);
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("isPM", isPm);
        return modelAndView;
    }

    /**
     * This method gets the request of a project creation.
     * Then calls {@link ProjectService}to manage it.
     * If bind resul has errors method will return a new view with these errors. if not, method will redirect to
     * gerProject method.
     * @param project {@link ProjectDom} object returned from view to
     * @return ModelAndView with redirection to get project view
     */
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView createProject(@ModelAttribute @Valid ProjectDom project, Principal principal,
                                      BindingResult result){
        if (result.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("createProject");
            modelAndView.addObject("project", project);
            modelAndView.addObject("reqTypes", projectService.getReqTypes());
            modelAndView.addObject("user", principal.getName());
            UserDom user = userService.findUser(principal.getName());
            List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
            boolean isPm = commonMethods.isPM(user,principal.getName());
            List<Group_user> groups = user.getGroups();
            modelAndView.addObject("groups", groups);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        ProjectDom projectDom = projectService.createProject(project);
        String path = "/project/"+projectDom.getIdProject();
        return new ModelAndView("redirect:"+path);
    }

    /**
     * This method gets the request of a projectUpdate.
     * Then calls {@link ProjectService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link ProjectDom} as object.
     * @param id {@link ProjectDom} id to find it in database.
     * @return ModelAndView with a project view and the persisted {@link ProjectDom} as object.
     */
    @RequestMapping(value = "/updateProject/{id}", method = RequestMethod.GET)
    public ModelAndView getUpdateView(@PathVariable int id, Principal principal){
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        ProjectDom projectDom = projectService.getProject(id);
        if(commonMethods.isAllowed(projectDomList,projectDom)) {
            ModelAndView modelAndView = new ModelAndView("updateProject");
            modelAndView.addObject("project", projectDom);
            modelAndView.addObject("reqTypes", projectService.getReqTypes());
            modelAndView.addObject("user", principal.getName());
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> groups = user.getGroups();
            modelAndView.addObject("groups", groups);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        else {
            LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to update project "+id);
            throw new AccessDeniedException("Not allowed");
        }
    }

    /**
     * This method receives a post request and calls {@link ProjectService} to update a project
     * Then Project Service returns information, that is updated in database.
     * @param id Project identifier
     * @param project Project Object Filled with information
     * @return ModelAndView with new project information, redirected to update project
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.POST)
    public ModelAndView updateProject(@PathVariable int id, @ModelAttribute @Valid ProjectDom project,
                                      Principal principal){
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        ProjectDom projectDom = projectService.getProject(id);
        if(commonMethods.isAllowed(projectDomList,projectDom)) {
            projectDom = projectService.updateProject(id, project);
            String path = "/project/" + projectDom.getIdProject();
            return new ModelAndView("redirect:" + path);
        }
        else  {
            LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to update project "+id);
            throw new AccessDeniedException("Not allowed");
        }
    }

    /**
     * Delete project receives a project id as path variable and uses it to call {@link ProjectService} delete method.
     * this method is called via JavaScript so returns a HttpStatus ok yif deletion has been done and a HttpStatus
     * Internal Server Error if not.
     * @param id project id to be deleted.
     * @return HttpStatus.ok if correct. HttpStatus.INTERNAL_SERVER_ERROR if not correct.
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProject(@PathVariable int id, Principal principal){
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        ProjectDom projectDom = projectService.getProject(id);
        if(commonMethods.isAllowed(projectDomList,projectDom)) {
            boolean deleted = projectService.deleteProject(id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.OK).body("");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        }
        else {
            LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to delete project "+id);
            throw new AccessDeniedException("Not allowed");
        }
    }

    /**
     * This method gets the request of a project.
     * Then calls {@link ProjectService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link ProjectDom} as object.
     * @param id {@link ProjectDom} id to find it in database.
     * @return ModelAndView with a project view and the persisted
     *       {@link ProjectDom} as object.
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ModelAndView getProject(@PathVariable int id, Principal principal){
        ProjectDom projectDom = projectService.getProject(id);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if(commonMethods.isAllowed(projectDomList,projectDom)) {
            ModelAndView modelAndView = new ModelAndView("project");
            projectDom.setGroupId(projectDom.getGroup().getGroupId());
            modelAndView.addObject("project", projectDom);
            modelAndView.addObject("fileList", documentService.getFileList(id, null));
            modelAndView.addObject("user", principal.getName());
            boolean isPm = commonMethods.isPM(user, principal.getName());
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        else {
            LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get project "+id);
            throw new AccessDeniedException("Not allowed");
        }
    }


}

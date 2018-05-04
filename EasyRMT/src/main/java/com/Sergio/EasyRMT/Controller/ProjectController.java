
/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class ProjectController {

    ProjectService projectService;
    DocumentService documentService;

    @Autowired
    public ProjectController(ProjectService projectService, DocumentService documentService) {
        this.projectService = projectService;
        this.documentService = documentService;
    }

    /**
     * This method returns the create project view
     * @param model with project object and an array with requirement types
     * @return ModelAndView which is model received as param
     */
    @RequestMapping(value="/createProject", method = RequestMethod.GET)
    public ModelAndView createProjectView(ModelAndView model, Principal principal){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom projectDom = new ProjectDom();
        model.setViewName("createProject");
        model.addObject("project", projectDom);
        model.addObject("reqTypes", projectService.getReqTypes());
        model.addObject("projectList", projectDomList);
        model.addObject("user", principal.getName());
        return model;
    }

    /**
     * This method gets the request of a project creation.
     * Then calls {@link ProjectService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link ProjectDom} as object.
     * @param project {@link ProjectDom} object returned from view to
     * @return ModelAndView with a project view and the persisted
     *       {@link ProjectDom} as object.
     */
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView createProject(@ModelAttribute @Valid ProjectDom project, Principal principal){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom projectDom = projectService.createProject(project);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("user", principal.getName());
        return modelAndView;
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
        List<ProjectDom> projectDomList = projectService.getProjects();
        ModelAndView modelAndView = new ModelAndView("updateProject");
        ProjectDom projectDom = projectService.getProject(id);
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("reqTypes", projectService.getReqTypes());
        modelAndView.addObject("user", principal.getName());
        return modelAndView;
    }

    /**
     * This method receives a post request and calls {@link ProjectService} to update a project
     * When Project Service returns informaction, that is updated in database.
     * @param id Project identificator
     * @param project Project Object Filled with information
     * @return ModelAndView with new project information, redirected to update project
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.POST)
    public ModelAndView updateProject(@PathVariable int id, @ModelAttribute @Valid ProjectDom project, Principal principal){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom projectDom = projectService.updateProject(id,project);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("user", principal.getName());
        return modelAndView;
    }

    /**
     * Delete project receives a project id as path variable and uses it to call {@link ProjectService} delete method.
     * this method is called via JavaScript so returns a HttpStatus ok yif deletion has been done and a HttpStatus
     * Internal Server Error if not.
     * @param id project id to be deleted.
     * @return HttpStatus.ok if correct. HttpStatus.INTERNAL_SERVER_ERROR if not correct.
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProject(@PathVariable int id){
        boolean deleted = projectService.deleteProject(id);
        if(deleted){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
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
        List<ProjectDom> projectDomList = projectService.getProjects();
        ModelAndView modelAndView = new ModelAndView("project");
        ProjectDom projectDom = projectService.getProject(id);
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("fileList", documentService.getFileList(id, null));
        modelAndView.addObject("user", principal.getName());
        return modelAndView;
    }


}

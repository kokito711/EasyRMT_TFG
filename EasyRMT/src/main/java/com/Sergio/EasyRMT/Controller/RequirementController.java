/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RequirementController {
    final String PATH_BASE = "/project/{projectId}/";
    ProjectService projectService;
    RequirementService requirementService;
    DocumentService documentService;

    @Autowired
    public RequirementController(ProjectService projectService, RequirementService requirementService,
                                 DocumentService documentService) {
        this.projectService = projectService;
        this.requirementService = requirementService;
        this.documentService = documentService;
    }

    /**
     * This rest controller receives a request to get a requirement list related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} id
     * @param mav autogenerated model and view
     * @return model and view with feature list
     */
    @RequestMapping(value = PATH_BASE+"requirements", method = RequestMethod.GET)
    public ModelAndView getRequirementListView(@PathVariable int projectId, ModelAndView mav){
        List<ProjectDom> projectDomList = projectService.getProjects();
        List<RequirementDom> requirementDomList = requirementService.getRequirements(projectId);
        ProjectDom project = projectService.getProject(projectId);
        List<RequirementTypeDom> reqTypes = projectService.getReqTypes();
        mav.setViewName("requirementsDashboard");
        mav.addObject("project", project);
        mav.addObject("requirementList", requirementDomList);
        mav.addObject("projectList", projectDomList);
        mav.addObject("reqTypes", reqTypes);
        return mav;
    }

    /**
     * This rest controller receives a request to get a requirement related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the requirement
     * @param requirementId {@link RequirementDom} id of requirement requested
     * @param mav autogenerated model and view
     * @return model and view with requirement
     */
    @RequestMapping(value = PATH_BASE+"requirement/{requirementId}", method = RequestMethod.GET)
    public ModelAndView getRequirementView(@PathVariable int projectId, @PathVariable int requirementId, ModelAndView mav){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom project = projectService.getProject(projectId);
        mav.setViewName("requirement");
        mav.addObject("requirement", requirementService.getRequirement(requirementId));
        mav.addObject("project", project);
        mav.addObject("projectList", projectDomList);
        mav.addObject("reqTypes", projectService.getReqTypes());
        mav.addObject("fileList", documentService.getFileList(projectId,requirementId));
        return mav;
    }

    /**
     * This rest controller receives a request to get a page to create a requirement related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the requirement
     * @param mav autogenerated model and view
     * @return model and view with page
     */
    @RequestMapping(value = PATH_BASE+"requirements/create", method = RequestMethod.GET)
    public ModelAndView getCreateRequirementView(@PathVariable int projectId, ModelAndView mav){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom project = projectService.getProject(projectId);
        RequirementDom requirementDom = new RequirementDom();
        mav.setViewName("createRequirement");
        mav.addObject("project", project);
        mav.addObject("projectList", projectDomList);
        mav.addObject("requirement", requirementDom);
        mav.addObject("priority", Priority.values());
        mav.addObject("state", State.values());
        mav.addObject("risk", Risk.values());
        mav.addObject("complexity", Complexity.values());
        mav.addObject("scope", Scope.values());
        return mav;
    }

    /**
     * This method gets the request of a requirement creation.
     * Then calls {@link RequirementService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link RequirementDom} as object.
     * @param projectId {@link ProjectDom} id object
     * @param requirementDom {@link RequirementDom} information to be persisted
     * @return ModelAndView with a project view and the persisted
     *       {@link RequirementDom} as object.
     */
    @RequestMapping(value = PATH_BASE+"requirements", method = RequestMethod.POST)
    public ModelAndView createRequirement(@PathVariable int projectId, @ModelAttribute @Valid RequirementDom requirementDom){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom project = projectService.getProject(projectId);
        RequirementDom persistedRequirement = requirementService.create(requirementDom, projectId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("requirement");
        mav.addObject("requirement", persistedRequirement);
        mav.addObject("project", project);
        mav.addObject("projectList", projectDomList);
        mav.addObject("reqTypes", projectService.getReqTypes());
        return mav;
    }

    /**
     * This rest controller receives a request to get a page to update a requirement related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the feature
     * @param requirementId {@link RequirementDom}  Id to be updated
     * @param mav autogenerated model and view
     * @return model and view with page
     */
    @RequestMapping(value = PATH_BASE+"requirement/update/{requirementId}", method = RequestMethod.GET)
    public ModelAndView getUpdateRequirementView(@PathVariable int projectId,@PathVariable int requirementId,
                                                 ModelAndView mav){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom project = projectService.getProject(projectId);
        RequirementDom requirementDom = requirementService.getRequirement(requirementId);
        mav.setViewName("updateRequirement");
        mav.addObject("project", project);
        mav.addObject("projectList", projectDomList);
        mav.addObject("requirement", requirementDom);
        mav.addObject("priority", Priority.values());
        mav.addObject("state", State.values());
        mav.addObject("risk", Risk.values());
        mav.addObject("complexity", Complexity.values());
        mav.addObject("scope", Scope.values());
        return mav;
    }

    /**
     * This method gets the request of a requirement update.
     * Then calls {@link RequirementService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link RequirementDom} as object.
     * @param projectId {@link ProjectDom} id object
     * @param requirementId {@link RequirementDom} Id to be updated
     * @param requirementDom {@link RequirementDom} information to be persisted
     * @return ModelAndView with a project view and the persisted
     *       {@link RequirementDom} as object.
     */
    @RequestMapping(value = PATH_BASE+"requirement/{requirementId}/update", method = RequestMethod.POST)
    public ModelAndView updateRequirement(@PathVariable int projectId, @PathVariable int requirementId,
                                   @ModelAttribute @Valid RequirementDom requirementDom){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom project = projectService.getProject(projectId);
        RequirementDom persistedRequirement = requirementService.update(requirementDom, requirementId,projectId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("requirement");
        mav.addObject("requirement", persistedRequirement);
        mav.addObject("project", project);
        mav.addObject("projectList", projectDomList);
        mav.addObject("reqTypes", projectService.getReqTypes());
        return mav;
    }

    /**
     * Delete requirement receives a requirement id as path variable and uses it to call {@link RequirementService}
     * delete method.
     * This method is called via JavaScript so returns a HttpStatus ok if deletion has been done and a HttpStatus
     * Internal Server Error if not.
     * @param projectId project id
     * @param requirementId requirement id to be deleted.
     * @return HttpStatus.ok if correct. HttpStatus.INTERNAL_SERVER_ERROR if not correct.
     */
    @RequestMapping(value = PATH_BASE+"/requirement/{requirementId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRequirement(@PathVariable int projectId, @PathVariable int requirementId){
        boolean deleted = requirementService.deleteRequirement(requirementId);
        if(deleted){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }
}
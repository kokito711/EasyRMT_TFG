/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.*;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FeatureController {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final String loggerMessage = "Unauthorized attempt to access: ";
    private final String PATH_BASE = "/project/{projectId}/";
    private ProjectService projectService;
    private FeatureService featureService;
    private DocumentService documentService;
    private CommonMethods commonMethods;
    private UserService userService;
    private TraceabilityService traceabilityService;
    private CommentService commentService;

    @Autowired
    public FeatureController(ProjectService projectService, FeatureService featureService, DocumentService documentService,
                             CommonMethods commonMethods, UserService userService, TraceabilityService traceabilityService,
                             CommentService commentService) {
        this.projectService = projectService;
        this.featureService = featureService;
        this.documentService = documentService;
        this.commonMethods = commonMethods;
        this.userService = userService;
        this.traceabilityService = traceabilityService;
        this.commentService = commentService;
    }

    /**
     * This rest controller receives a request to get a feature list related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} id
     * @param modelAndView autogenerated model and view
     * @return model and view with feature list
     */
    @RequestMapping(value = PATH_BASE+"features", method = RequestMethod.GET)
    public ModelAndView getFeatureListView(@PathVariable int projectId, ModelAndView modelAndView, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            List<FeatureDom> featureDomList = featureService.getFeatures(projectId);
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> group = project.getGroup().getUsers();
            modelAndView.setViewName("featuresDashboard");
            modelAndView.addObject("project", project);
            modelAndView.addObject("featureList", featureDomList);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a list of features from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This rest controller receives a request to get a feature related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the epic
     * @param featureId {@link FeatureDom} FeatureId of feature requested
     * @param modelAndView autogenerated model and view
     * @return model and view with feature
     */
    @RequestMapping(value = PATH_BASE+"feature/{featureId}", method = RequestMethod.GET)
    public ModelAndView getFeatureView(@PathVariable int projectId, @PathVariable int featureId, ModelAndView modelAndView,
                                       Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean isPm = commonMethods.isPM(user, principal.getName());
            boolean isStakeholder = commonMethods.isStakeholder(user, principal.getName(), project);
            List<Group_user> group = project.getGroup().getUsers();
            TraceDom traceability = traceabilityService.getTraceability(featureId);
            TraceDom extension = new TraceDom();
            List<CommentDom> comments = commentService.getComments(featureId);
            CommentDom comment = new CommentDom();
            modelAndView.setViewName("feature");
            modelAndView.addObject("feature",featureService.getFeature(featureId));
            modelAndView.addObject("project", project);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("fileList", documentService.getFileList(projectId,featureId));
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            modelAndView.addObject("traceability", traceability);
            modelAndView.addObject("traceObject",extension );
            modelAndView.addObject("reqTypes", project.getRequirementTypes());
            modelAndView.addObject("reqsNotTraced", traceabilityService.getNotTracedReqs(projectId,featureId));
            modelAndView.addObject("featureList", traceabilityService.getNotTracedFeatures(projectId, featureId));
            modelAndView.addObject("useCaseList", traceabilityService.getNotTracedUseCases(projectId,featureId));
            modelAndView.addObject("comments", comments);
            modelAndView.addObject("comment", comment);
            modelAndView.addObject("isStakeholder", isStakeholder);
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to obtain a feature from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This rest controller receives a request to get a page to create a feature related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the feature
     * @param modelAndView autogenerated model and view
     * @return model and view with page
     */
    @RequestMapping(value = PATH_BASE+"features/create", method = RequestMethod.GET)
    public ModelAndView getCreateFeatureView(@PathVariable int projectId, ModelAndView modelAndView, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> group = project.getGroup().getUsers();
            FeatureDom featureDom = new FeatureDom();
            modelAndView.setViewName("createFeature");
            modelAndView.addObject("project", project);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("feature", featureDom);
            modelAndView.addObject("priority", Priority.values());
            modelAndView.addObject("state", State.values());
            modelAndView.addObject("risk", Risk.values());
            modelAndView.addObject("complexity", Complexity.values());
            modelAndView.addObject("scope", Scope.values());
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to create a feature in project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This method gets the request of a feature creation.
     * Then calls {@link FeatureService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link FeatureDom} as object.
     * @param projectId {@link ProjectDom} id object
     * @param feature {@link FeatureDom} information to be persisted
     * @return ModelAndView with a project view and the persisted
     *       {@link FeatureDom} as object.
     */
    @RequestMapping(value = PATH_BASE+"features", method = RequestMethod.POST)
    public ModelAndView createFeature(@PathVariable int projectId, @ModelAttribute @Valid FeatureDom feature,
                                      Principal principal,BindingResult result){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        feature.setAuthorId(user.getUserId());
        if (commonMethods.isAllowed(projectDomList, project)) {
            if (result.hasErrors()) {
                boolean isPm = commonMethods.isPM(user, principal.getName());
                List<Group_user> group = project.getGroup().getUsers();
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("createFeature");
                modelAndView.addObject("feature", feature);
                modelAndView.addObject("project", project);
                modelAndView.addObject("projectList", projectDomList);
                modelAndView.addObject("user", principal.getName());
                modelAndView.addObject("group", group);
                modelAndView.addObject("isPM", isPm);
                return modelAndView;
            }
            FeatureDom persistedFeature = featureService.create(feature, projectId);
            String path = "/project/"+projectId+"/feature/"+persistedFeature.getIdFeature();
            return new ModelAndView("redirect:"+path);
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to create a feature in project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This rest controller receives a request to get a page to update a feature related with a project
     * Then creates the model and view and returns it.
     * @param projectId {@link ProjectDom} project Id which owns the feature
     * @param featureId {@link FeatureDom} feature Id to be updated
     * @param modelAndView autogenerated model and view
     * @return model and view with page
     */
    @RequestMapping(value = PATH_BASE+"feature/update/{featureId}", method = RequestMethod.GET)
    public ModelAndView getUpdateFeatureView(@PathVariable int projectId,@PathVariable int featureId, ModelAndView modelAndView, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            FeatureDom featureDom = featureService.getFeature(featureId);
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> group = project.getGroup().getUsers();
            modelAndView.setViewName("updateFeature");
            modelAndView.addObject("project", project);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("feature", featureDom);
            modelAndView.addObject("priority", Priority.values());
            modelAndView.addObject("state", State.values());
            modelAndView.addObject("risk", Risk.values());
            modelAndView.addObject("complexity", Complexity.values());
            modelAndView.addObject("scope", Scope.values());
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to update a feature from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This method gets the request of a feature update.
     * Then calls {@link FeatureService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link FeatureDom} as object.
     * @param projectId {@link ProjectDom} id object
     * @param featureId {@link FeatureDom} feature Id to be updated
     * @param featureDom {@link FeatureDom} information to be persisted
     * @return ModelAndView with a project view and the persisted
     *       {@link FeatureDom} as object.
     */
    @RequestMapping(value = PATH_BASE+"feature/{featureId}/update", method = RequestMethod.POST)
    public ModelAndView updateFeature(@PathVariable int projectId, @PathVariable int featureId,
                                   @ModelAttribute @Valid FeatureDom featureDom, Principal principal,
                                      BindingResult result){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            if (result.hasErrors()) {
                boolean isPm = commonMethods.isPM(user, principal.getName());
                List<Group_user> group = project.getGroup().getUsers();
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("updateFeature");
                modelAndView.addObject("feature", featureDom);
                modelAndView.addObject("project", project);
                modelAndView.addObject("projectList", projectDomList);
                modelAndView.addObject("user", principal.getName());
                modelAndView.addObject("group", group);
                modelAndView.addObject("isPM", isPm);
                return modelAndView;
            }
            FeatureDom persistedFeature = featureService.update(featureDom, featureId, projectId);
            String path = "/project/"+projectId+"/feature/"+persistedFeature.getIdFeature();
            return new ModelAndView("redirect:"+path);
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to update a feature from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * Delete feature receives a feature id as path variable and uses it to call {@link FeatureService} delete method.
     * this method is called via JavaScript so returns a HttpStatus ok if deletion has been done and a HttpStatus
     * Internal Server Error if not.
     * @param projectId project id
     * @param featureId feature id to be deleted.
     * @return HttpStatus.ok if correct. HttpStatus.INTERNAL_SERVER_ERROR if not correct.
     */
    @RequestMapping(value = PATH_BASE+"/feature/{featureId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFeature(@PathVariable int projectId, @PathVariable int featureId,Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            boolean deleted = featureService.deleteFeature(featureId);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.OK).body("");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to delete a feature from project "+projectId);
        throw new AccessDeniedException("Not allowed");
    }
}

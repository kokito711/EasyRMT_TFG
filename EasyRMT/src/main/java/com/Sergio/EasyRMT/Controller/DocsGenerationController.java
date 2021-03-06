/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.TraceDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.*;
import com.Sergio.EasyRMT.configuration.Tuple;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class DocsGenerationController {

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final String loggerMessage = "Unauthorized attempt to access: ";
    private final String PRINTER_PATH_BASE = "/print/{projectId}/";
    private final String DOCX_PATH_BASE = "/file/docx/{projectId}/";
    private final String PDF_PATH_BASE = "/file/pdf/{projectId}/";
    private ProjectService projectService;
    private FeatureService featureService;
    private EpicService epicService;
    private CommonMethods commonMethods;
    private UserService userService;
    private UseCaseService useCaseService;
    private TraceabilityService traceabilityService;
    private UserStoryService userStoryService;
    private RequirementService requirementService;
    private DocsService docsService;
    private CommentService commentService;

    @Autowired
    public DocsGenerationController(ProjectService projectService, FeatureService featureService, EpicService epicService,
                                    CommonMethods commonMethods, UserService userService, UseCaseService useCaseService,
                                    TraceabilityService traceabilityService, UserStoryService userStoryService,
                                    RequirementService requirementService, DocsService docsService, CommentService commentService) {
        this.projectService = projectService;
        this.featureService = featureService;
        this.epicService = epicService;
        this.commonMethods = commonMethods;
        this.userService = userService;
        this.useCaseService = useCaseService;
        this.traceabilityService = traceabilityService;
        this.userStoryService = userStoryService;
        this.requirementService = requirementService;
        this.docsService = docsService;
        this.commentService = commentService;
    }

    /**
     * This method obtains a request to generate a page that will be printable
     * @param projectId id of project
     * @param type type of element
     * @param objectId id of object to be printed
     * @param principal element with login information
     * @return ModelAndView with printable page
     */
    @RequestMapping(value = PRINTER_PATH_BASE+"{type}/{objectId}", method = RequestMethod.GET)
    public ModelAndView getPrinterPage (@PathVariable int projectId,@PathVariable String type,
                                        @PathVariable int objectId,Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            ModelAndView modelAndView = new ModelAndView("printPage");
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> group = project.getGroup().getUsers();
            TraceDom extension = new TraceDom();
            TraceDom traceability= traceabilityService.getTraceability(objectId);
            List<CommentDom> comments = commentService.getComments(objectId);
            populateModelAndView(type, objectId, modelAndView);
            modelAndView.addObject("project", project);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            modelAndView.addObject("traceability", traceability);
            modelAndView.addObject("traceObject",extension );
            modelAndView.addObject("reqTypes", project.getRequirementTypes());
            modelAndView.addObject("comments", comments);
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document to print from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    /**
     * This method obtains a request to generate a page that will be printable
     * @param projectId id of project
     * @param type type of element
     * @param objectId id of object to be printed
     * @param principal element with login information
     * @return ModelAndView with printable page
     */
    @RequestMapping(value = PRINTER_PATH_BASE+"{type}/list/{objectId}", method = RequestMethod.GET)
    public ModelAndView getListPrinterPage (@PathVariable int projectId, @PathVariable String type,
                                            @PathVariable int objectId, Principal principal){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            ModelAndView modelAndView = new ModelAndView("printListPage");
            boolean isPm = commonMethods.isPM(user, principal.getName());
            List<Group_user> group = project.getGroup().getUsers();
            populateListModelAndView(project, type, objectId, modelAndView);
            modelAndView.addObject("project", project);
            modelAndView.addObject("projectList", projectDomList);
            modelAndView.addObject("user", principal.getName());
            modelAndView.addObject("group", group);
            modelAndView.addObject("isPM", isPm);
            modelAndView.addObject("type", type);
            modelAndView.addObject("reqTypes", project.getRequirementTypes());
            return modelAndView;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document to print from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }


    @RequestMapping(value = DOCX_PATH_BASE+"{type}/{objectId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getObjectAsDocx(@PathVariable int projectId, @PathVariable String type,
                                              @PathVariable int objectId, Principal principal, Locale locale){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            File file = null;
            try {
                file = docsService.generateDocx(project, type, objectId, locale);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename="+ file.getName());
            //Converting file to bytearray
            byte[] content = new byte[(int) file.length()];
            convertFileToByteArray(file, content);
            header.setContentLength(content.length);
            HttpEntity<byte[]> response = new HttpEntity<>(content, header);
            Path root = Paths.get(file.getPath());
            boolean status = false;
            try {
                status = Files.deleteIfExists(root);
            }
            catch (Exception e){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getPath());
            }
            if(!status){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getPath());
            }
            return response;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    @RequestMapping(value = DOCX_PATH_BASE+"{type}/list/{objectId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getObjectListAsDocx(@PathVariable int projectId, @PathVariable String type,
                                              @PathVariable int objectId, Principal principal, Locale locale){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            File file = null;
            try {
                file = docsService.generateListDocx(project, type, objectId, locale);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename="+ file.getName());
            //Converting file to bytearray
            byte[] content = new byte[(int) file.length()];
            convertFileToByteArray(file, content);
            header.setContentLength(content.length);
            HttpEntity<byte[]> response = new HttpEntity<>(content, header);
            Path root = Paths.get(file.getPath());
            boolean status = false;
            try {
                status = Files.deleteIfExists(root);
            }
            catch (Exception e){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getPath());
            }
            if(!status){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getPath());
            }
            return response;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    @RequestMapping(value = PDF_PATH_BASE+"{type}/{objectId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getObjectAsPdf(@PathVariable int projectId, @PathVariable String type,
                                              @PathVariable int objectId, Principal principal, Locale locale){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            Tuple<String,byte[]> file = null;
            try {
                file = docsService.generatePdf(project, type, objectId, locale);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/pdf"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename="+ file.getKey());
            //Converting file to bytearray
            byte[] content = file.getContent();
            header.setContentLength(content.length);
            HttpEntity<byte[]> response = new HttpEntity<>(content, header);
            Path root = Paths.get(file.getKey());
            boolean status = false;
            try {
                status = Files.deleteIfExists(root);
            }
            catch (Exception e){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getKey());
            }
            if(!status){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getKey());
            }
            return response;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    @RequestMapping(value = PDF_PATH_BASE+"{type}/list/{objectId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getObjectListAsPdf(@PathVariable int projectId, @PathVariable String type,
                                                  @PathVariable int objectId, Principal principal, Locale locale){
        ProjectDom project = projectService.getProject(projectId);
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        if (commonMethods.isAllowed(projectDomList, project)) {
            Tuple<String,byte[]> file = null;
            try {
                file = docsService.generateListPdf(project, type, objectId, locale);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType("application/pdf"));
            header.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename="+ file.getKey());
            //Converting file to bytearray
            byte[] content = file.getContent();
            header.setContentLength(content.length);
            HttpEntity<byte[]> response = new HttpEntity<>(content, header);
            Path root = Paths.get(file.getKey());
            boolean status = false;
            try {
                status = Files.deleteIfExists(root);
            }
            catch (Exception e){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getKey());
            }
            if(!status){
                LOGGER.log(Level.SEVERE, "System failed to delete file. File path: "+file.getKey());
            }
            return response;
        }
        LOGGER.log(Level.INFO, loggerMessage+"User "+principal.getName()+" has tried to get a document from project "
                +projectId);
        throw new AccessDeniedException("Not allowed");
    }

    private void convertFileToByteArray(File file, byte[] content) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(content);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void populateListModelAndView(ProjectDom project, String type, int objectId, ModelAndView modelAndView) {
        switch (type){
            case "feature":
                if(objectId==0) {
                    modelAndView.addObject("object", featureService.getFeatures(project.getIdProject()));
                }
                else {
                    modelAndView.addObject("object", useCaseService.getUseCases(objectId));
                }
                break;
            case "epic":
                if(objectId==0) {
                    modelAndView.addObject("object", epicService.getEpics(objectId));
                }
                else {
                    modelAndView.addObject("object", userStoryService.getUserStory(objectId));
                }
                break;
            case "usecase":
                modelAndView.addObject("object",useCaseService.getByProjectID(project.getIdProject()));
                break;
            case "userstory":
                modelAndView.addObject("object",userStoryService.getByProjectID(project.getIdProject()));
                break;
            case "requirement":
                if(objectId==0) {
                    modelAndView.addObject("object", requirementService.getRequirements(project.getIdProject()));
                }
                else {
                    throw new AccessDeniedException("Not allowed");
                }
                break;
            default:
                throw new AccessDeniedException("Not allowed");
        }
    }


    private void populateModelAndView(String type, int objectId, ModelAndView modelAndView) {
        switch (type){
            case "feature":
                modelAndView.addObject("object",featureService.getFeature(objectId));
                break;
            case "epic":
                modelAndView.addObject("object",epicService.getEpic(objectId));
                break;
            case "usecase":
                modelAndView.addObject("object",useCaseService.getUseCase(objectId));
                break;
            case "userstory":
                modelAndView.addObject("object",userStoryService.getUserStory(objectId));
                break;
            case "requirement":
                modelAndView.addObject("object",requirementService.getRequirement(objectId));
            default:
                throw new AccessDeniedException("Not allowed");
        }
    }
}

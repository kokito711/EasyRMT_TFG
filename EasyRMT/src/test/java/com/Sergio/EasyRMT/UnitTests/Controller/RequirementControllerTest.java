/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.RequirementController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.RequirementService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RequirementControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    RequirementService requirementService;
    @Mock
    DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        requirementService = mock(RequirementService.class);
        documentService = mock(DocumentService.class);
    }

    @Test
    @DisplayName("Request a view with a list of requirements")
    public void getRequirementListView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        List<RequirementDom> requirementDomList = mock(List.class);
        ProjectDom project = mock(ProjectDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(requirementService.getRequirements(anyInt())).thenReturn(requirementDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(projectService.getReqTypes()).thenReturn(reqTypes);

        ModelAndView expected = new ModelAndView("epicsDashboard");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirementList", requirementDomList);
        expected.addObject("project", project);
        expected.addObject("reqTypes", reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getRequirementListView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("requirementList").equals(expected.getModel().get("requirementList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(requirementService,times(1)).getRequirements(19);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //Verify getReqTypes has been called
        verify(projectService, times(1)).getReqTypes();

    }

    @Test
    @DisplayName("Request a view with a requirement")
    public void getRequirementView_ProjectIdAndRequirementIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom project = mock(ProjectDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(requirementService.getRequirement(anyInt())).thenReturn(requirementDom);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(projectService.getReqTypes()).thenReturn(reqTypes);

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("project", project);
        expected.addObject("reqTypes", reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getRequirementView(19,19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("requirement").equals(expected.getModel().get("requirement")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(requirementService,times(1)).getRequirement(19);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //Verify getReqTypes has been called
        verify(projectService, times(1)).getReqTypes();
    }

    @Test
    @DisplayName("Request a view with a createRequirement view")
    public void getCreateRequirementView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        RequirementDom requirementDom = new RequirementDom();
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("createRequirement");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getCreateRequirementView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("requirement").equals(expected.getModel().get("requirement")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
    }

    @Test
    @DisplayName("Request a view with a updateRequirement view")
    public void getUpdateRequirementView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(requirementService.getRequirement(anyInt())).thenReturn(requirementDom);

        ModelAndView expected = new ModelAndView("updateRequirement");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getUpdateRequirementView(19,1, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("requirement").equals(expected.getModel().get("requirement")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //verify getReqs is called 1 time
        verify(requirementService,times(1)).getRequirement(1);
    }

    @Test
    @DisplayName("createRequirement method returns a modelAndView object")
    public void createRequirement_ProjectIdAndRequirementDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom project = mock(ProjectDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(requirementService.create(requirementDom, 17)).thenReturn(requirementDom);
        when(projectService.getReqTypes()).thenReturn(reqTypes);

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("project", project);
        expected.addObject("reqTypes", reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.createRequirement(17,requirementDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("requirement").equals(expected.getModel().get("requirement")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(requirementService,times(1)).create(requirementDom,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //Verify getReqTypes has been called
        verify(projectService, times(1)).getReqTypes();
    }

    @Test
    @DisplayName("updateRequirement method returns a modelAndView object")
    public void updateRequirement_ProjectIdAndReqIdAndRequirementDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom project = mock(ProjectDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(requirementService.update(requirementDom,1, 17)).thenReturn(requirementDom);
        when(projectService.getReqTypes()).thenReturn(reqTypes);

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("project", project);
        expected.addObject("reqTypes", reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.updateRequirement(17,1,requirementDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("requirement").equals(expected.getModel().get("requirement")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(requirementService,times(1)).update(requirementDom,1,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //Verify getReqTypes has been called
        verify(projectService, times(1)).getReqTypes();
    }

    @Test
    @DisplayName("deleteRequirement method returns an http.ok when requirement is deleted")
    public void deleteReq_idProjectAndRequirementIdProvided_RequirementDeleted_ReturnsOk(){
        when(requirementService.deleteRequirement(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        RequirementController requirementController = createRequirementController();

        ResponseEntity obtained = requirementController.deleteRequirement(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(requirementService,times(1)).deleteRequirement(1);
    }
    @Test
    @DisplayName("deleteRequirement method returns an http.Internal_Server_Error when requirement is not deleted")
    public void deleteReq_idProjectAndRequirementIdProvided_RequirementDeleted_ReturnsInternalServerError(){
        when(requirementService.deleteRequirement(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        RequirementController requirementController = createRequirementController();

        ResponseEntity obtained = requirementController.deleteRequirement(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(requirementService,times(1)).deleteRequirement(1);
    }

    private RequirementController createRequirementController(){
        return new RequirementController(projectService, requirementService, documentService);
    }
}

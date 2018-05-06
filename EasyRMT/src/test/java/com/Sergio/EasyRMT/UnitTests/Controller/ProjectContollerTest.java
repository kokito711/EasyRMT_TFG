package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.ProjectController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectContollerTest {
/*
    @Mock
    ProjectService projectService;
    @Mock
    DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        documentService = mock(DocumentService.class);
    }
    @Test
    @DisplayName("createProjectView method returns a modelAndView object")
    public void createProjectView_MAVProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();

        ModelAndView expected = new ModelAndView("createProject");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", projectService.getReqTypes());
        when(projectService.getProjects()).thenReturn(projectDomList);

        ProjectController projectController = createProjectController();

        //ModelAndView obtained = projectController.createProjectView(new ModelAndView());

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqTypes is called 2 times(1 test, 1 method)
        verify(projectService,times(2)).getReqTypes();*//*
    }
    @Test
    @DisplayName("createProject method returns a modelAndView object")
    public void createProject_ProjectDomProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = mock(ProjectDom.class);

        ModelAndView expected = new ModelAndView("project");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.createProject(projectDom)).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.createProject(projectDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify create project is called
        verify(projectService,times(1)).createProject(projectDom);*//*
    }

    @Test
    @DisplayName("getUpdateView method returns a modelAndView object")
    public void updateProjectView_idProjectProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = mock(ProjectDom.class);

        ModelAndView expected = new ModelAndView("updateProject");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", projectService.getReqTypes());
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.getUpdateView(7);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("reqTypes").equals(expected.getModel().get("reqTypes")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify project Service getProject is called
        verify(projectService,times(1)).getProject(7);
        //verify getReqTypes is called (1test, 1 service)
        verify(projectService,times(2)).getReqTypes();*//*
    }

    @Test
    @DisplayName("updateProject method returns a modelAndView object")
    public void updateProject_idProjectProjectDomProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = mock(ProjectDom.class);

        ModelAndView expected = new ModelAndView("project");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.updateProject(7,projectDom)).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.updateProject(7,projectDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify update project is called
        verify(projectService,times(1)).updateProject(7,projectDom);*//*
    }

    @Test
    @DisplayName("deleteProject method returns an http.ok when project is deleted")
    public void deleteProject_idProjectProvided_ProjectDeleted_ReturnsOk(){
        when(projectService.deleteProject(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        ProjectController projectController = createProjectController();

        ResponseEntity obtained = projectController.deleteProject(7);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(projectService,times(1)).deleteProject(7);
    }
    @Test
    @DisplayName("deleteProject method returns an http.Internal_Server_Error when project is not deleted")
    public void deleteProject_idProjectProvided_DeletionFails_ReturnsInternalServerError(){
        when(projectService.deleteProject(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        ProjectController projectController = createProjectController();

        ResponseEntity obtained = projectController.deleteProject(7);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(projectService,times(1)).deleteProject(7);
    }

    @Test
    @DisplayName("getProject method returns a modelAndView object")
    public void getProject_idProjectProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = mock(ProjectDom.class);

        ModelAndView expected = new ModelAndView("project");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(7)).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.getProject(7);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify update project is called
        verify(projectService,times(1)).getProject(7);*//*
    }
   *//* private ProjectController createProjectController(){
        return new ProjectController(projectService, documentService);
    }*/
}

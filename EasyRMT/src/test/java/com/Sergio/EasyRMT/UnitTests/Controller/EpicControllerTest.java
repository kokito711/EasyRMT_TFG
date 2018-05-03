/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.EpicController;
import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.EpicService;
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

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    EpicService epicService;
    @Mock
    DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        epicService = mock(EpicService.class);
        documentService = mock(DocumentService.class);
    }

    @Test
    @DisplayName("Request a view with a list of epics")
    public void getEpicListView_ProjectIdProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        List<EpicDom> epicDomList = mock(List.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(epicService.getEpics(anyInt())).thenReturn(epicDomList);

        ModelAndView expected = new ModelAndView("epicsDashboard");
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicList", epicDomList);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getEpicListView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("epicList").equals(expected.getModel().get("epicList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getEpics is called 1 time
        verify(epicService,times(1)).getEpics(19);*/

    }

    @Test
    @DisplayName("Request a view with an epic")
    public void getEpicView_ProjectIdAndEpicIdProvided_ReturnView(){
       /* List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(epicService.getEpic(anyInt())).thenReturn(epicDom);

        ModelAndView expected = new ModelAndView("epic");
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getEpicView(19,1, new ModelAndView());

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("epic").equals(expected.getModel().get("epic")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify getEpic is called
        verify(epicService,times(1)).getEpic(1);*/
    }

    @Test
    @DisplayName("Request a view with a createEpic view")
    public void getCreateEpicView_ProjectIdProvided_ReturnView(){
       /* List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = new EpicDom();
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("createEpic");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getCreateEpicView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epic").equals(expected.getModel().get("epic")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());*/
    }

    @Test
    @DisplayName("Request a view with a updateEpic view")
    public void getUpdateEpicView_ProjectIdProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.getEpic(anyInt())).thenReturn(epicDom);

        ModelAndView expected = new ModelAndView("updateEpic");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getUpdateEpicView(19,1, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epic").equals(expected.getModel().get("epic")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //verify getReqs is called 1 time
        verify(epicService,times(1)).getEpic(1);*/
    }

    @Test
    @DisplayName("createEpic method returns a modelAndView object")
    public void createEpic_ProjectIdAndEpicDomProvided_ReturnsMAV(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.create(epicDom, 17)).thenReturn(epicDom);

        ModelAndView expected = new ModelAndView("epic");
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);
        expected.addObject("project", project);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.createEpic(17,epicDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("epic").equals(expected.getModel().get("epic")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(epicService,times(1)).create(epicDom,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());*/
    }

    @Test
    @DisplayName("updateEpic method returns a modelAndView object")
    public void updateEpic_ProjectIdAndEpicIdAndEpicDomProvided_ReturnsMAV(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.update(epicDom,1, 17)).thenReturn(epicDom);

        ModelAndView expected = new ModelAndView("epic");
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);
        expected.addObject("project", project);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.updateEpic(17,1,epicDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("epic").equals(expected.getModel().get("epic")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(epicService,times(1)).update(epicDom,1,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());*/
    }

    @Test
    @DisplayName("deleteEpic method returns an http.ok when epic is deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicDeleted_ReturnsOk(){
        when(epicService.deleteEpic(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        EpicController epicController = createEpicController();

        ResponseEntity obtained = epicController.deleteEpic(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(epicService,times(1)).deleteEpic(1);
    }
    @Test
    @DisplayName("deleteEpic method returns an http.Internal_Server_Error when epic is not deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicDeleted_ReturnsInternalServerError(){
        when(epicService.deleteEpic(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        EpicController epicController = createEpicController();

        ResponseEntity obtained = epicController.deleteEpic(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(epicService,times(1)).deleteEpic(1);
    }

    private EpicController createEpicController(){
        return new EpicController(projectService,epicService, documentService);
    }
}

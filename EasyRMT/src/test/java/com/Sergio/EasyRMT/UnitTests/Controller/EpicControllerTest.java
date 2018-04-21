/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.EpicController;
import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.EpicService;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        epicService = mock(EpicService.class);
    }

    //TODO refactor this test
    @Test
    @DisplayName("Request a view with a list of epics")
    public void getEpicListView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
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
        verify(epicService,times(1)).getEpics(19);

    }

    @Test
    @DisplayName("Request a view with an epic")
    public void getEpicView_ProjectIdAndEpicIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
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
        verify(epicService,times(1)).getEpic(1);
    }

    private EpicController createEpicController(){
        return new EpicController(projectService,epicService);
    }
}

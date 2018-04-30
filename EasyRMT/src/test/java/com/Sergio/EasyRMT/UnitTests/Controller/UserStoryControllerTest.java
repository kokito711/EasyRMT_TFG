/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.EpicController;
import com.Sergio.EasyRMT.Controller.UserStoryController;
import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.UserStory;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.EpicService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserStoryService;
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
public class UserStoryControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    UserStoryService userStoryService;
    @Mock
    EpicService epicService;
    @Mock
    DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        userStoryService = mock(UserStoryService.class);
        epicService = mock(EpicService.class);
        documentService = mock(DocumentService.class);
    }

    @Test
    @DisplayName("Request a view with a list of user stories")
    public void getUserStoryListView_ProjectIdAndEpicIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        EpicDom epicDom =  mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(userStoryService.getUserStories(anyInt())).thenReturn(userStoryDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("userStoriesDashboard");
        expected.addObject("project", project);
        expected.addObject("userStoriesList", userStoryDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.getUserStoriesListView(19,1, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStoriesList").equals(expected.getModel().get("userStoriesList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).getUserStories(1);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("Request a view with a list of user stories")
    public void getUserStoryListView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(userStoryService.getByProjectID(anyInt())).thenReturn(userStoryDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("userStoriesDashboardProject");
        expected.addObject("project", project);
        expected.addObject("userStoriesList", userStoryDomList);
        expected.addObject("projectList", projectDomList);

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.getUserStoriesListView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStoriesList").equals(expected.getModel().get("userStoriesList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).getByProjectID(19);
    }

    @Test
    @DisplayName("Request a view with an UserStory")
    public void getUserStoryView_ProjectIdAndEpicIdAndUserStoryIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        EpicDom epicDom =  mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(userStoryService.getUserStory(anyInt())).thenReturn(userStoryDom);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("project", project);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.getUserStoryView(19,1, 1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStory").equals(expected.getModel().get("userStory")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).getUserStory(1);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("Request a view with a createUserStory view")
    public void getCreateUserStoryView_ProjectIdAndEpicIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        UserStoryDom userStoryDom = new UserStoryDom();
        ProjectDom project = mock(ProjectDom.class);
        EpicDom epicDom =  mock(EpicDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("createUserStory");
        expected.addObject("project", project);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.getCreateUserStoryView(19,1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStory").equals(expected.getModel().get("userStory")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("Request a view with a updateUserStory view")
    public void getUpdateUserStoryView_ProjectIdProvidedAndEpicIdAndUserStoryDomProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        ProjectDom project = mock(ProjectDom.class);
        EpicDom epicDom =  mock(EpicDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(userStoryService.getUserStory(anyInt())).thenReturn(userStoryDom);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("updateUserStory");
        expected.addObject("project", project);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.getUpdateUserStoryView(19,1,1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStory").equals(expected.getModel().get("userStory")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).getUserStory(1);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("createUserStory method returns a modelAndView object")
    public void createUserStory_ProjectIdAndEpicIdAndUserStoryDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(userStoryService.create(userStoryDom,1, 19)).thenReturn(userStoryDom);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("project", project);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.createUserStory(19,1, userStoryDom);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStory").equals(expected.getModel().get("userStory")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).create(userStoryDom,1,19);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("updateUserStory method returns a modelAndView object")
    public void updateUserStory_ProjectIdAndEpicIdAndUserStoryIdAndUserStoryDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom project = mock(ProjectDom.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(userStoryService.update(19,1, 1,userStoryDom)).thenReturn(userStoryDom);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        doReturn("Epic Name").when(epicDom).getName();
        doReturn(1).when(epicDom).getIdEpic();

        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("project", project);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId",1);
        expected.addObject("epicName", "Epic Name");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.updateUserStory(19,1,1, userStoryDom);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("userStory").equals(expected.getModel().get("userStory")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("epicId").equals(expected.getModel().get("epicId")));
        assertTrue(obtained.getModel().get("epicName").equals(expected.getModel().get("epicName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(userStoryService,times(1)).update(19,1,1,userStoryDom);
        //verify epic service has been called
        verify(epicService, times(1)).getEpic(1);
        //verify epicDom has been called
        verify(epicDom, times(1)).getIdEpic();
        verify(epicDom, times(1)).getName();
    }

    @Test
    @DisplayName("deleteUserStory method returns an http.ok when UserStory is deleted")
    public void deleteUserStory_idProjectAndEpicIdAndUserStoryIdProvided_UserStoryDeleted_ReturnsOk(){
        when(userStoryService.deleteUserStory(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        UserStoryController userStoryController = createUserStoryController();

        ResponseEntity obtained = userStoryController.deleteUserStory(7,1,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(userStoryService,times(1)).deleteUserStory(1);
    }
    @Test
    @DisplayName("deleteUserStory method returns an http.Internal_Server_Error when UserStory is not deleted")
    public void deleteUserStory_idProjectAndEpicIdAndUserStoryIdProvided_UserStoryDeleted_ReturnsInternalServerError(){
        when(userStoryService.deleteUserStory(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        UserStoryController userStoryController = createUserStoryController();

        ResponseEntity obtained = userStoryController.deleteUserStory(7,1,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(userStoryService,times(1)).deleteUserStory(1);
    }

    private UserStoryController createUserStoryController(){
        return new UserStoryController(projectService, epicService, userStoryService, documentService);
    }
}

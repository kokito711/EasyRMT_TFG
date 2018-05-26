/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.UserStoryController;
import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserStoryControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private EpicService epicService;
    @Mock
    private UserStoryService userStoryService;
    @Mock
    private DocumentService documentService;
    @Mock
    private CommonMethods commonMethods;
    @Mock
    private UserService userService;
    @Mock
    private TraceabilityService traceabilityService;
    @Mock
    private CommentService commentService;
    @Mock
    private Principal principal;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        userStoryService = mock(UserStoryService.class);
        epicService = mock(EpicService.class);
        documentService = mock(DocumentService.class);
        commonMethods = mock(CommonMethods.class);
        userService = mock(UserService.class);
        traceabilityService = mock(TraceabilityService.class);
        commentService = mock(CommentService.class);
        principal = mock(Principal.class);
        bindingResult = mock(BindingResult.class);
    }

    @Test
    @DisplayName("Request a view with a list of user stories")
    public void getUserStoryListView_ProjectIdAndEpicIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        EpicDom epicDom = mock(EpicDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(epicService.getEpic(anyInt())).thenReturn(epicDom);
        when(userStoryService.getUserStories(1)).thenReturn(userStoryDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(epicDom.getIdEpic()).thenReturn(1);
        when(epicDom.getName()).thenReturn("epic");

        ModelAndView expected = new ModelAndView("userStoriesDashboard");
        expected.addObject("project", projectDom);
        expected.addObject("userStoriesList", userStoryDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epic");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView obtained = userStoryController.getUserStoriesListView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userStoriesList"), expected.getModel().get("userStoriesList"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(epicService,times(1)).getEpic(1);
        verify(userStoryService, times(1)).getUserStories(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UserStories throws an AccessDeniedException")
    public void getUserStoryListView_ProjectIdProvidedAndEpicIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.getUserStoriesListView(1,1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(epicService,times(0)).getEpic(1);
        verify(userStoryService, times(0)).getUserStories(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UserStories")
    public void getUserStoryListView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(userStoryService.getByProjectID(1)).thenReturn(userStoryDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("userStoriesDashboardProject");
        expected.addObject("project", projectDom);
        expected.addObject("userStoriesList", userStoryDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView obtained = userStoryController.getUserStoriesListView(1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("useCasesList"), expected.getModel().get("useCasesList"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(userStoryService, times(1)).getByProjectID(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UserStories from project throws an AccessDeniedException")
    public void getUserStoryListView_ProjectIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.getUserStoriesListView(1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(userStoryService, times(0)).getByProjectID(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with an UserStory")
    public void getUserStoryView_ProjectIdAndEpicIdAndUserStoryIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        EpicDom epicDom = mock(EpicDom.class);
        TraceDom traceability = mock(TraceDom.class);
        List<CommentDom> comments = mock(List.class);
        List<DocumentationDom> fileList = mock(List.class);
        List<RequirementDom> notTraced = mock(List.class);
        List<EpicDom> epicDomList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(commonMethods.isStakeholder(user,"user", projectDom)).thenReturn(false);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(traceabilityService.getTraceability(1)).thenReturn(traceability);
        when(commentService.getComments(1)).thenReturn(comments);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        when(documentService.getFileList(1,1)).thenReturn(fileList);
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);
        when(traceabilityService.getNotTracedReqs(1,1)).thenReturn(notTraced);
        when(traceabilityService.getNotTracedEpics(1,1)).thenReturn(epicDomList);
        when(traceabilityService.getNotTracedUserStories(1,1)).thenReturn(userStoryDomList);
        when(userStoryService.getUserStory(1)).thenReturn(userStoryDom);
        when(epicDom.getIdEpic()).thenReturn(1);
        when(epicDom.getName()).thenReturn("epicDom");
        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("userStory", userStoryDom);
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epicDom");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("fileList", fileList);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("traceability", traceability);
        expected.addObject("traceObject", new TraceDom());
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("reqsNotTraced", notTraced);
        expected.addObject("epicList", epicDomList);
        expected.addObject("userStoryList", userStoryDomList);
        expected.addObject("comments", comments);
        expected.addObject("comment", new CommentDom());
        expected.addObject("isStakeholder", false);

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView obtained = userStoryController.getUserStoryView(1,1, 1,new ModelAndView(),principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userStory"), expected.getModel().get("userStory"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("reqTypes"), expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("fileList"), expected.getModel().get("fileList"));
        assertEquals(obtained.getModel().get("user"), expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("group"), expected.getModel().get("group"));
        assertEquals(obtained.getModel().get("isPM"), expected.getModel().get("isPM"));
        assertEquals(obtained.getModel().get("traceability"), expected.getModel().get("traceability"));
        assertEquals(obtained.getModel().get("traceObject"), expected.getModel().get("traceObject"));
        assertEquals(obtained.getModel().get("reqsNotTraced"), expected.getModel().get("reqsNotTraced"));
        assertEquals(obtained.getModel().get("epicList"), expected.getModel().get("epicList"));
        assertEquals(obtained.getModel().get("userStoryList"), expected.getModel().get("userStoryList"));
        assertEquals(obtained.getModel().get("comments"), expected.getModel().get("comments"));
        assertEquals(obtained.getModel().get("comment"), expected.getModel().get("comment"));
        assertEquals(obtained.getModel().get("isStakeholder"), expected.getModel().get("isStakeholder"));
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(commonMethods, times(1)).isStakeholder(user, "user", projectDom);
        verify(projectDom, times(1)).getGroup();
        verify(group, times(1)).getUsers();
        verify(traceabilityService, times(1)).getTraceability(1);
        verify(commentService, times(1)).getComments(1);
        verify(epicService,times(1)).getEpic(1);
        verify(documentService, times(1)).getFileList(1,1);
        verify(principal, times(4)).getName();
        verify(projectDom, times(1)).getRequirementTypes();
        verify(traceabilityService, times(1)).getNotTracedReqs(1,1);
        verify(traceabilityService, times(1)).getNotTracedEpics(1,1);
        verify(traceabilityService, times(1)).getNotTracedUserStories(1,1);
        verify(userStoryService, times(1)).getUserStory(1);
    }

    @Test
    @DisplayName("Request a view with a UserStory throws an AccessDeniedException")
    public void getUserStoryView_ProjectIdAndAndEpicIdAndUseCaseProvided_ThrowsException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);
        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.getUserStoryView(1,1, 1,new ModelAndView(),principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }

        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(commonMethods, times(0)).isStakeholder(user, "user", projectDom);
        verify(projectDom, times(0)).getGroup();
        verify(group, times(0)).getUsers();
        verify(traceabilityService, times(0)).getTraceability(1);
        verify(commentService, times(0)).getComments(1);
        verify(epicService,times(0)).getEpic(1);
        verify(documentService, times(0)).getFileList(1,1);
        verify(principal, times(2)).getName();
        verify(projectDom, times(0)).getRequirementTypes();
        verify(traceabilityService, times(0)).getNotTracedReqs(1,1);
        verify(traceabilityService, times(0)).getNotTracedEpics(1,1);
        verify(traceabilityService, times(0)).getNotTracedUserStories(1,1);
        verify(userStoryService, times(0)).getUserStory(1);
    }

    @Test
    @DisplayName("Request a view with a createUserStory view")
    public void getCreateUserStoryView_ProjectIdAndEpicIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        EpicDom epic = mock(EpicDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(epicService.getEpic(1)).thenReturn(epic);
        when(epic.getIdEpic()).thenReturn(1);
        when(epic.getName()).thenReturn("epic");

        ModelAndView expected = new ModelAndView("createUserStory");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("userStory", new UserStoryDom());
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epic");
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "epic");

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView obtained = userStoryController.getCreateUserStoryView(1,1, new ModelAndView(), principal);
        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("userStory"), expected.getModel().get("userStory"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("user"),expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("group"),expected.getModel().get("group"));
        assertEquals(obtained.getModel().get("isPM"),expected.getModel().get("isPM"));

        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(projectDom, times(1)).getGroup();
        verify(group, times(1)).getUsers();
        verify(principal, times(3)).getName();
        verify(epicService, times(1)).getEpic(1);
    }

    @Test
    @DisplayName("Request a view with a createUserStory view not allowed")
    public void getCreateUserStoryView_ProjectIdAndFeatureIdProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.getCreateUserStoryView(1,1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(group, times(0)).getUsers();
        verify(principal, times(2)).getName();
        verify(epicService, times(0)).getEpic(1);
    }

    @Test
    @DisplayName("Request a view with a updateUserStory view")
    public void getUpdateUserStoryView_ProjectIdProvidedAndEpicIdAndUserStoryDomProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        EpicDom epic = mock(EpicDom.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(userStoryService.getUserStory(1)).thenReturn(userStoryDom);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(epicService.getEpic(1)).thenReturn(epic);
        when(epic.getIdEpic()).thenReturn(1);
        when(epic.getName()).thenReturn("epic");

        ModelAndView expected = new ModelAndView("updateUserStory");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("userStory", userStoryDom);
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epic");
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "epic");

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView obtained = userStoryController.getUpdateUserStoryView(1,1, 1,new ModelAndView(), principal);
        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("userStory"), expected.getModel().get("userStory"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("user"),expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("group"),expected.getModel().get("group"));
        assertEquals(obtained.getModel().get("isPM"),expected.getModel().get("isPM"));

        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(projectDom, times(1)).getGroup();
        verify(group, times(1)).getUsers();
        verify(principal, times(3)).getName();
        verify(epicService, times(1)).getEpic(1);
        verify(userStoryService,times(1)).getUserStory(1);
    }

    @Test
    @DisplayName("Request a view with a UserStory view access denied")
    public void getUpdateUserStoryView_ProjectIdAndFeatureIdAndUserStoryDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.getUpdateUserStoryView(1,1, 1,new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(group, times(0)).getUsers();
        verify(principal, times(2)).getName();
        verify(epicService, times(0)).getEpic(1);
        verify(userStoryService, times(0)).getUserStory(1);
    }

    @Test
    @DisplayName("createUserStory method returns a modelAndView redirect")
    public void createUserStory_ProjectIdAndEpicIdAndUserStoryDomProvided_ReturnsRedirect(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);
        when(userStoryService.create(userStoryDom,1,1)).thenReturn(userStoryDom);

        ModelAndView expected = new ModelAndView("redirect: /project/1/epic/1/userstory/1");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView obtained = userStoryController.createUserStory(1,1,principal,userStoryDom, bindingResult);
        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertTrue(obtained.getModel().isEmpty());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(principal, times(1)).getName();
        verify(epicService, times(0)).getEpic(1);
        verify(userStoryService, times(1)).create(userStoryDom,1,1);
    }

    @Test
    @DisplayName("createFeature method returns a modelAndView object")
    public void createUserStory_ProjectIdAndEpicIdAndUserStoryProvided_ReturnsErrorMAV(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        UserStoryDom userStory = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        when(epicDom.getIdEpic()).thenReturn(1);
        when(epicDom.getName()).thenReturn("epic");

        UserStoryController userStoryController = createUserStoryController();
        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("userStory", userStory);
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epic");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = userStoryController.createUserStory(1,1,principal,userStory, bindingResult);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("userStory"), expected.getModel().get("userStory"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("user"),expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("group"),expected.getModel().get("group"));
        assertEquals(obtained.getModel().get("reqTypes"),expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("isPM"),expected.getModel().get("isPM"));
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(projectDom, times(1)).getGroup();
        verify(principal, times(3)).getName();
        verify(epicService, times(1)).getEpic(1);
        verify(userStoryService, times(0)).create(userStory,1,1);
    }

    @Test
    @DisplayName("createUserStory method returns not allowed")
    public void createUserStory_ProjectIdAndEpicIdAndUserStoryDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.createUserStory(1,1,principal,userStoryDom, bindingResult);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(principal, times(2)).getName();
        verify(epicService, times(0)).getEpic(1);
        verify(userStoryService, times(0)).create(userStoryDom,1,1);
    }

    @Test
    @DisplayName("updateUserStory method returns a modelAndView object")
    public void updateUserStory_ProjectIdAndEpicIdAndUserStoryIdAndUserStoryDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        EpicDom epicDom = mock(EpicDom.class);
        UserStoryDom userStoryDom = mock(UserStoryDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        when(epicDom.getIdEpic()).thenReturn(1);
        when(epicDom.getName()).thenReturn("feature");
        when(userStoryService.update(1,1,1,userStoryDom)).thenReturn(userStoryDom);

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/epic/1/userstory/1");
        ModelAndView obtained = userStoryController.updateUserStory(1,1, 1,userStoryDom, bindingResult,principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertTrue(obtained.getModel().isEmpty());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(principal, times(1)).getName();
        verify(epicService, times(0)).getEpic(1);
        verify(userStoryService, times(1)).update(1,1,1,userStoryDom);
    }

    @Test
    @DisplayName("updateUserStory method returns a modelAndView object")
    public void updateUserStory_ProjectIdAndUserStoryIdAndUserStoryDomProvided_ReturnsMAV(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        UserStoryDom userStory = mock(UserStoryDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        when(epicDom.getIdEpic()).thenReturn(1);
        when(epicDom.getName()).thenReturn("epic");

        UserStoryController userStoryController = createUserStoryController();

        ModelAndView expected = new ModelAndView("userStory");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("userStory", userStory);
        expected.addObject("epicId", 1);
        expected.addObject("epicName", "epic");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = userStoryController.updateUserStory(1,1, 1,userStory, bindingResult,principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("userStory"), expected.getModel().get("userStory"));
        assertEquals(obtained.getModel().get("epicId"), expected.getModel().get("epicId"));
        assertEquals(obtained.getModel().get("epicName"), expected.getModel().get("epicName"));
        assertEquals(obtained.getModel().get("user"),expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("group"),expected.getModel().get("group"));
        assertEquals(obtained.getModel().get("reqTypes"),expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("isPM"),expected.getModel().get("isPM"));
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(projectDom, times(1)).getGroup();
        verify(principal, times(3)).getName();
        verify(epicService, times(1)).getEpic(1);
        verify(userStoryService, times(0)).update(1,1,1,userStory);
    }

    @Test
    @DisplayName("updateUserStory method returns not allowed")
    public void updateUserStory_ProjectIdAndUserStoryDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        UserStoryDom userStory = mock(UserStoryDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();

        try {
            userStoryController.updateUserStory(1,1, 1,userStory, bindingResult,principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(principal, times(2)).getName();
        verify(epicService, times(0)).getEpic(1);
        verify(userStoryService, times(0)).update(1,1,1,userStory);
    }

    @Test
    @DisplayName("deleteUserStory method returns an http.ok when UserStory is deleted")
    public void deleteUserStory_idProjectAndEpicIdAndUserStoryIdProvided_UserStoryDeleted_ReturnsOk(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(userStoryService.deleteUserStory(1)).thenReturn(true);

        UserStoryController userStoryController = createUserStoryController();
        ResponseEntity obtained = userStoryController.deleteUserStory(1,1,1,principal);

        //Test conditions
        assertEquals(HttpStatus.OK,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(userStoryService,times(1)).deleteUserStory(1);
    }
    @Test
    @DisplayName("deleteUserStory method returns an http.Internal_Server_Error when UserStory is not deleted")
    public void deleteUserStory_idProjectAndEpicIdAndUserStoryIdProvided_UserStoryDeleted_ReturnsInternalServerError(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(userStoryService.deleteUserStory(1)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        ResponseEntity obtained = userStoryController.deleteUserStory(1,1,1,principal);

        //Test conditions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(userStoryService,times(1)).deleteUserStory(1);
    }

    @Test
    @DisplayName("deleteUseCase method throws exception when UseCase is not deleted")
    public void deleteUseCase_idProjectAndFeatureIdAndUseCaseIdProvided_UseCaseNotDeleted_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UserStoryController userStoryController = createUserStoryController();
        try {
            userStoryController.deleteUserStory(1,1,1,principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(2)).getName();
        verify(userStoryService,times(0)).deleteUserStory(1);
    }

    private UserStoryController createUserStoryController(){
        return new UserStoryController(projectService, epicService, userStoryService, documentService, commonMethods,
                userService, traceabilityService, commentService);
    }
}

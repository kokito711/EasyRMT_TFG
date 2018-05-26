/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.EpicController;
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private EpicService epicService;
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
    @DisplayName("Request a view with a list of epics")
    public void getEpicListView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<EpicDom> epicDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(epicService.getEpics(anyInt())).thenReturn(epicDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("epicsDashboard");
        expected.addObject("project", projectDom);
        expected.addObject("epicList", epicDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getEpicListView(1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("epicList"), expected.getModel().get("epicList"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(epicService,times(1)).getEpics(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of epics throws an AccessDeniedException")
    public void getEpicListView_ProjectIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        EpicController epicController = createEpicController();
        try {
            epicController.getEpicListView(1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(epicService,times(0)).getEpics(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with an epic")
    public void getEpicView_ProjectIdAndEpicIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        EpicDom epic = mock(EpicDom.class);
        TraceDom traceability = mock(TraceDom.class);
        List<CommentDom> comments = mock(List.class);
        List<DocumentationDom> fileList = mock(List.class);
        List<RequirementDom> notTraced = mock(List.class);
        List<EpicDom> epicDomList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
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
        when(epicService.getEpic(1)).thenReturn(epic);
        when(documentService.getFileList(1,1)).thenReturn(fileList);
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);
        when(traceabilityService.getNotTracedReqs(1,1)).thenReturn(notTraced);
        when(traceabilityService.getNotTracedEpics(1,1)).thenReturn(epicDomList);
        when(traceabilityService.getNotTracedUserStories(1,1)).thenReturn(userStoryDomList);
        ModelAndView expected = new ModelAndView("epic");
        expected.addObject("epic", epic);
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

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getEpicView(1,1, new ModelAndView(),principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("epic"), expected.getModel().get("epic"));
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
        verify(traceabilityService, times(0)).getNotTracedFeatures(1,1);
        verify(traceabilityService, times(0)).getNotTracedUseCases(1,1);
    }

    @Test
    @DisplayName("Request a view with an epic throws an AccessDeniedException")
    public void getEpicView_ProjectIdAndEpicIdProvided_ThrowsException(){
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
        EpicController epicController = createEpicController();
        try {
            epicController.getEpicView(1,1, new ModelAndView(),principal);
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
        verify(traceabilityService, times(0)).getNotTracedFeatures(1,1);
        verify(traceabilityService, times(0)).getNotTracedUseCases(1,1);

    }

    @Test
    @DisplayName("Request a view with a createEpic view")
    public void getCreateEpicView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("createEpic");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", new EpicDom());
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getCreateEpicView(1, new ModelAndView(), principal);
        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("epic"), expected.getModel().get("epic"));
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
    }

    @Test
    @DisplayName("Request a view with a createEpic view not allowed")
    public void getCreateEpicView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        EpicController epicController = createEpicController();

        try {
            epicController.getCreateEpicView(1, new ModelAndView(),principal);
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
    }

    @Test
    @DisplayName("Request a view with a updateEpic view")
    public void getUpdateEpicView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        EpicDom epicDom = mock(EpicDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(epicService.getEpic(1)).thenReturn(epicDom);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("updateEpic");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);


        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.getUpdateEpicView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("epic"), expected.getModel().get("epic"));
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
    }

    @Test
    @DisplayName("Request a view with a updateEpic view access denied")
    public void getUpdateEpicView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        EpicController epicController = createEpicController();

        try {
            epicController.getUpdateEpicView(1,1, new ModelAndView(), principal);
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
    }

    @Test
    @DisplayName("createEpic method returns a modelAndView redirect")
    public void createEpic_ProjectIdAndEpicDomProvided_ReturnsRedirect(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);
        when(epicService.create(epicDom,1)).thenReturn(epicDom);
        when(epicDom.getIdEpic()).thenReturn(1);

        ModelAndView expected = new ModelAndView("redirect: /project/1/epic/1");

        EpicController epicController = createEpicController();

        ModelAndView obtained = epicController.createEpic(1,epicDom,principal,bindingResult);
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
        verify(epicService, times(1)).create(epicDom, 1);
    }

    @Test
    @DisplayName("createEpic method returns a modelAndView object")
    public void createEpic_ProjectIdAndEpicDomProvided_ReturnsErrorMAV(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        EpicController epicController = createEpicController();

        ModelAndView expected = new ModelAndView("createEpic");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = epicController.createEpic(1,epicDom, principal,result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("epic"), expected.getModel().get("epic"));
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
        verify(epicService, times(0)).create(epicDom, 1);
    }

    @Test
    @DisplayName("createEpic method returns not allowed")
    public void createEpic_ProjectIdAndEpicDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        EpicDom epicDom = mock(EpicDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        EpicController epicController = createEpicController();

        try {
            epicController.createEpic(1,epicDom, principal,result);
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
        verify(epicService, times(0)).create(epicDom, 1);
    }

    @Test
    @DisplayName("updateEpic method returns a modelAndView redirect")
    public void updateEpic_ProjectIdAndEpicIdAndEpicDomProvided_ReturnsRedirect(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);
        when(epicService.update(epicDom,1,1)).thenReturn(epicDom);
        when(epicDom.getIdEpic()).thenReturn(1);

        EpicController epicController = createEpicController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/epic/1");
        ModelAndView obtained = epicController.updateEpic(1,1, epicDom, principal, result);

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
        verify(epicService, times(1)).update(epicDom, 1,1);
    }

    @Test
    @DisplayName("updateEpic method returns a modelAndView object")
    public void updateEpic_ProjectIdAndEpicIdAndEpicDomProvided_ReturnsMAV(){
        EpicDom epicDom = mock(EpicDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        EpicController epicController = createEpicController();

        ModelAndView expected = new ModelAndView("updateEpic");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("epic", epicDom);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = epicController.updateEpic(1,1, epicDom, principal, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("epic"), expected.getModel().get("epic"));
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
        verify(epicService, times(0)).update(epicDom, 1,1);
    }

    @Test
    @DisplayName("updateEpic method returns not allowed")
    public void updateEpic_ProjectIdAndEpicDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        EpicDom epicDom = mock(EpicDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        EpicController epicController = createEpicController();

        try {
            epicController.updateEpic(1,1, epicDom, principal, result);
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
        verify(epicService, times(0)).update(epicDom, 1,1);
    }

    @Test
    @DisplayName("deleteEpic method returns an http.ok when Epic is deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicDeleted_ReturnsOk(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(epicService.deleteEpic(1)).thenReturn(true);

        EpicController epicController = createEpicController();
        ResponseEntity obtained = epicController.deleteEpic(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.OK,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(epicService,times(1)).deleteEpic(1);
    }

    @Test
    @DisplayName("deleteEpic method returns an http.INTERNAL_SERVER_ERROR when Epic is not deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicNotDeleted_ReturnsInternalServerError(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(epicService.deleteEpic(1)).thenReturn(false);

        EpicController epicController = createEpicController();
        ResponseEntity obtained = epicController.deleteEpic(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(epicService,times(1)).deleteEpic(1);
    }

    @Test
    @DisplayName("deleteEpic method throws exception when Epic is not deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicNotDeleted_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        EpicController epicController = createEpicController();
        try {
            epicController.deleteEpic(1,1, principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(2)).getName();
        verify(epicService,times(0)).deleteEpic(1);
    }

    private EpicController createEpicController(){
        return new EpicController(projectService,epicService, documentService, commonMethods, userService,
                traceabilityService, commentService);
    }
}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.RequirementController;
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
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RequirementControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private RequirementService requirementService;
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

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        requirementService = mock(RequirementService.class);
        documentService = mock(DocumentService.class);
        commonMethods = mock(CommonMethods.class);
        userService = mock(UserService.class);
        traceabilityService = mock(TraceabilityService.class);
        commentService = mock(CommentService.class);
        principal = mock(Principal.class);
    }

    @Test
    @DisplayName("Request a view with a list of requirements")
    public void getRequirementListView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<RequirementDom> requirements = mock(List.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(requirementService.getRequirements(1)).thenReturn(requirements);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectService.getReqTypes()).thenReturn(reqTypes);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("epicsDashboard");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirementList", requirements);
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getRequirementListView(1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("requirementList"), expected.getModel().get("requirementList"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("reqTypes"), expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(requirementService,times(1)).getRequirements(1);
        verify(projectService, times(1)).getReqTypes();
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of requirements failed due not a valid user")
    public void getRequirementListView_CredentialsNotOk_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        RequirementController requirementController = createRequirementController();

        try {
            requirementController.getRequirementListView(1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(requirementService,times(0)).getRequirements(1);
        verify(projectService, times(0)).getReqTypes();
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a requirement in an agile project")
    public void getAgileRequirementView_ProjectIdAndRequirementIdProvided_ReturnView(){

        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        TraceDom traceability = mock(TraceDom.class);
        List<CommentDom> comments = mock(List.class);
        List<DocumentationDom> files = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        List<RequirementDom> notTraced = mock(List.class);
        List<EpicDom> notTracedEpics = mock(List.class);
        List<UserStoryDom> notTracedUserStories = mock(List.class);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(commonMethods.isStakeholder(user, "user", projectDom)).thenReturn(false);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(traceabilityService.getTraceability(1)).thenReturn(traceability);
        when(commentService.getComments(1)).thenReturn(comments);
        when(requirementService.getRequirement(1)).thenReturn(requirementDom);
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);
        when(documentService.getFileList(1,1)).thenReturn(files);
        when(traceabilityService.getNotTracedReqs(1,1)).thenReturn(notTraced);
        when(traceabilityService.getNotTracedEpics(1,1)).thenReturn(notTracedEpics);
        when(traceabilityService.getNotTracedUserStories(1,1)).thenReturn(notTracedUserStories);
        when(projectDom.getType()).thenReturn(ProjectType.AGILE);

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("fileList", files);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("traceability", traceability);
        expected.addObject("traceObject", new TraceDom());
        expected.addObject("reqsNotTraced",notTraced);
        expected.addObject("epicList", notTracedEpics);
        expected.addObject("userStoryList", notTracedUserStories);
        expected.addObject("comments", comments);
        expected.addObject("comment", new CommentDom());
        expected.addObject("isStakeholder", false);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getRequirementView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
        verify(requirementService,times(1)).getRequirement(1);
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
    @DisplayName("Request a view with a requirement in a traditional project")
    public void getTraditionalRequirementView_ProjectIdAndRequirementIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        TraceDom traceability = mock(TraceDom.class);
        List<CommentDom> comments = mock(List.class);
        List<DocumentationDom> files = mock(List.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        List<RequirementDom> notTraced = mock(List.class);
        List<FeatureDom> notTracedFeatures = mock(List.class);
        List<UseCaseDom> notTracedUseCases = mock(List.class);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(commonMethods.isStakeholder(user, "user", projectDom)).thenReturn(false);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(traceabilityService.getTraceability(1)).thenReturn(traceability);
        when(commentService.getComments(1)).thenReturn(comments);
        when(requirementService.getRequirement(1)).thenReturn(requirementDom);
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);
        when(documentService.getFileList(1,1)).thenReturn(files);
        when(traceabilityService.getNotTracedReqs(1,1)).thenReturn(notTraced);
        when(traceabilityService.getNotTracedFeatures(1,1)).thenReturn(notTracedFeatures);
        when(traceabilityService.getNotTracedUseCases(1,1)).thenReturn(notTracedUseCases);
        when(projectDom.getType()).thenReturn(ProjectType.NOT_AGILE);

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("fileList", files);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("traceability", traceability);
        expected.addObject("traceObject", new TraceDom());
        expected.addObject("reqsNotTraced",notTraced);
        expected.addObject("featureList", notTracedFeatures);
        expected.addObject("useCaseList", notTracedUseCases);
        expected.addObject("comments", comments);
        expected.addObject("comment", new CommentDom());
        expected.addObject("isStakeholder", false);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getRequirementView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
        assertEquals(obtained.getModel().get("featureList"), expected.getModel().get("featureList"));
        assertEquals(obtained.getModel().get("useCaseList"), expected.getModel().get("useCaseList"));
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
        verify(requirementService,times(1)).getRequirement(1);
        verify(documentService, times(1)).getFileList(1,1);
        verify(principal, times(4)).getName();
        verify(projectDom, times(1)).getRequirementTypes();
        verify(traceabilityService, times(1)).getNotTracedReqs(1,1);
        verify(traceabilityService, times(0)).getNotTracedEpics(1,1);
        verify(traceabilityService, times(0)).getNotTracedUserStories(1,1);
        verify(traceabilityService, times(1)).getNotTracedFeatures(1,1);
        verify(traceabilityService, times(1)).getNotTracedUseCases(1,1);
    }

    @Test
    @DisplayName("Request a view with a requirement in a traditional project not allowed")
    public void getTraditionalRequirementView_ProjectIdAndRequirementIdProvided_AccessDeniedExceptionThrown(){
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
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(commonMethods.isStakeholder(user, "user", projectDom)).thenReturn(false);


        RequirementController requirementController = createRequirementController();

        try {
            requirementController.getRequirementView(1,1, new ModelAndView(), principal);
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
        verify(requirementService,times(0)).getRequirement(1);
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
    @DisplayName("Request a view with a createRequirement view")
    public void getCreateRequirementView_ProjectIdProvided_ReturnView(){
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

        ModelAndView expected = new ModelAndView("createRequirement");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", new RequirementDom());
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getCreateRequirementView(1, new ModelAndView(),principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
    @DisplayName("Request a view with a createRequirement view not allowed")
    public void getCreateRequirementView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        RequirementController requirementController = createRequirementController();

        try {
            requirementController.getCreateRequirementView(1, new ModelAndView(),principal);
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
    @DisplayName("Request a view with a updateRequirement view")
    public void getUpdateRequirementView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(requirementService.getRequirement(1)).thenReturn(requirementDom);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("createRequirement");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        RequirementController requirementController = createRequirementController();

        ModelAndView obtained = requirementController.getUpdateRequirementView(1,1, new ModelAndView(),principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
    @DisplayName("Request a view with a updateRequirement view not allowed")
    public void getUpdateRequirementView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        RequirementController requirementController = createRequirementController();

        try {
            requirementController.getUpdateRequirementView(1,1, new ModelAndView(),principal);
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
    @DisplayName("createRequirement method returns a modelAndView redirect")
    public void createRequirement_ProjectIdAndRequirementDomProvided_ReturnsRedirect(){
        RequirementDom requirementDom = mock(RequirementDom.class);
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
        when(requirementService.create(requirementDom,1)).thenReturn(requirementDom);
        when(requirementDom.getIdRequirement()).thenReturn(1);

        RequirementController requirementController = createRequirementController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/requirement/1");

        ModelAndView obtained = requirementController.createRequirement(1,requirementDom, result, principal);

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
        verify(requirementService, times(1)).create(requirementDom, 1);
    }

    @Test
    @DisplayName("createRequirement method returns a modelAndView object")
    public void createRequirement_ProjectIdAndRequirementDomProvided_ReturnsErrorMAV(){
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
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
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = requirementController.createRequirement(1,requirementDom, result, principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
        verify(requirementService, times(0)).create(requirementDom, 1);

    }

    @Test
    @DisplayName("createRequirement method returns not allowed")
    public void createRequirement_ProjectIdAndRequirementDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        BindingResult result = mock(BindingResult.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        RequirementController requirementController = createRequirementController();

        try {
            requirementController.createRequirement(1,requirementDom, result,principal);
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
        verify(requirementService, times(0)).create(requirementDom, 1);
    }

    @Test
    @DisplayName("updateRequirement method returns a modelAndView redirect")
    public void updateRequirement_ProjectIdAndReqIdAndRequirementDomProvided_Returnsredirect(){
        RequirementDom requirementDom = mock(RequirementDom.class);
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
        when(requirementService.update(requirementDom,1,1)).thenReturn(requirementDom);
        when(requirementDom.getIdRequirement()).thenReturn(1);

        RequirementController requirementController = createRequirementController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/requirement/1");

        ModelAndView obtained = requirementController.updateRequirement(1,1, principal,requirementDom, result);

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
        verify(requirementService, times(1)).update(requirementDom, 1,1);
    }

    @Test
    @DisplayName("updateRequirement method returns a modelAndView object")
    public void updateRequirement_ProjectIdAndReqIdAndRequirementDomProvided_ReturnsMAV(){
        RequirementDom requirementDom = mock(RequirementDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
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
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);

        RequirementController requirementController = createRequirementController();

        ModelAndView expected = new ModelAndView("requirement");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("requirement", requirementDom);
        expected.addObject("reqTypes", reqTypes);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = requirementController.updateRequirement(1,1, principal,requirementDom, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("requirement"), expected.getModel().get("requirement"));
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
        verify(requirementService, times(0)).update(requirementDom, 1,1);
    }

    @Test
    @DisplayName("updateRequirement method returns not allowed")
    public void updateRequirement_ProjectIdAndRequirementDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        RequirementDom requirementDom = mock(RequirementDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        RequirementController requirementController = createRequirementController();

        try {
            requirementController.updateRequirement(1,1, principal,requirementDom, result);
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
        verify(requirementService, times(0)).update(requirementDom, 1,1);
    }

    @Test
    @DisplayName("deleteRequirement method returns an http.ok when requirement is deleted")
    public void deleteReq_idProjectAndRequirementIdProvided_RequirementDeleted_ReturnsOk(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(requirementService.deleteRequirement(1)).thenReturn(true);

        RequirementController requirementController = createRequirementController();

        ResponseEntity obtained = requirementController.deleteRequirement(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.OK,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(requirementService,times(1)).deleteRequirement(1);
    }

    @Test
    @DisplayName("deleteRequirement method returns an http.INTERNAL_SERVER_ERROR when requirement is not deleted")
    public void deleteReq_idProjectAndRequirementIdProvided_RequirementNotDeleted_ReturnsInternalServerError(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(requirementService.deleteRequirement(1)).thenReturn(false);

        RequirementController requirementController = createRequirementController();

        ResponseEntity obtained = requirementController.deleteRequirement(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(requirementService,times(1)).deleteRequirement(1);
    }

    @Test
    @DisplayName("deleteRequirement method throws exception when requirement is not deleted")
    public void deleteReq_idProjectAndRequirementIdProvided_RequirementNotDeleted_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        RequirementController requirementController = createRequirementController();
        try {
            requirementController.deleteRequirement(1,1, principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(2)).getName();
        verify(requirementService,times(0)).deleteRequirement(1);
    }

    private RequirementController createRequirementController(){
        return new RequirementController(projectService, requirementService, documentService,
                commonMethods, userService, traceabilityService, commentService);
    }
}

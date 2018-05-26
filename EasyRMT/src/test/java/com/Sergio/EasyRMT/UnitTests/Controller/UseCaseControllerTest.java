/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.UseCaseController;
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
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UseCaseControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private UseCaseService useCaseService;
    @Mock
    private FeatureService featureService;
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
        useCaseService = mock(UseCaseService.class);
        featureService = mock(FeatureService.class);
        documentService = mock(DocumentService.class);
        commonMethods = mock(CommonMethods.class);
        userService = mock(UserService.class);
        traceabilityService = mock(TraceabilityService.class);
        commentService = mock(CommentService.class);
        principal = mock(Principal.class);
        bindingResult = mock(BindingResult.class);
    }

    @Test
    @DisplayName("Request a view with a list of UseCases")
    public void getUseCaseListView_ProjectIdAndFeatureIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.getFeature(anyInt())).thenReturn(featureDom);
        when(useCaseService.getUseCases(1)).thenReturn(useCaseDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(featureDom.getIdFeature()).thenReturn(1);
        when(featureDom.getName()).thenReturn("feature");

        ModelAndView expected = new ModelAndView("useCasesDashboard");
        expected.addObject("project", projectDom);
        expected.addObject("useCasesList", useCaseDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        UseCaseController useCaseController = createUseCaseController();
        ModelAndView obtained = useCaseController.getUseCasesListView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("useCasesList"), expected.getModel().get("useCasesList"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(featureService,times(1)).getFeature(1);
        verify(useCaseService, times(1)).getUseCases(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UseCases throws an AccessDeniedException")
    public void getUseCaseListView_ProjectIdProvidedAndFeatureIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.getUseCasesListView(1,1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(featureService,times(0)).getFeatures(1);
        verify(useCaseService, times(0)).getUseCases(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UseCases")
    public void getUseCaseListView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(useCaseService.getByProjectID(1)).thenReturn(useCaseDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("useCasesDashboard");
        expected.addObject("project", projectDom);
        expected.addObject("useCasesList", useCaseDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        UseCaseController useCaseController = createUseCaseController();
        ModelAndView obtained = useCaseController.getUseCasesListView(1, new ModelAndView(), principal);

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
        verify(useCaseService, times(1)).getByProjectID(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of UseCases from project throws an AccessDeniedException")
    public void getUseCaseListView_ProjectIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.getUseCasesListView(1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(useCaseService, times(0)).getByProjectID(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with an UseCase")
    public void getUseCaseView_ProjectIdAndFeatureIdAndUseCaseIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        FeatureDom feature = mock(FeatureDom.class);
        TraceDom traceability = mock(TraceDom.class);
        List<CommentDom> comments = mock(List.class);
        List<DocumentationDom> fileList = mock(List.class);
        List<RequirementDom> notTraced = mock(List.class);
        List<FeatureDom> featureDomList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        List<RequirementTypeDom> reqTypes = mock(List.class);
        UseCaseDom useCase = mock(UseCaseDom.class);
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
        when(featureService.getFeature(1)).thenReturn(feature);
        when(documentService.getFileList(1,1)).thenReturn(fileList);
        when(projectDom.getRequirementTypes()).thenReturn(reqTypes);
        when(traceabilityService.getNotTracedReqs(1,1)).thenReturn(notTraced);
        when(traceabilityService.getNotTracedFeatures(1,1)).thenReturn(featureDomList);
        when(traceabilityService.getNotTracedUseCases(1,1)).thenReturn(useCaseDomList);
        when(useCaseService.getUseCase(1)).thenReturn(useCase);
        when(feature.getIdFeature()).thenReturn(1);
        when(feature.getName()).thenReturn("feature");
        ModelAndView expected = new ModelAndView("useCase");
        expected.addObject("useCase", useCase);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
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
        expected.addObject("featureList", featureDomList);
        expected.addObject("useCaseList", useCaseDomList);
        expected.addObject("comments", comments);
        expected.addObject("comment", new CommentDom());
        expected.addObject("isStakeholder", false);

        UseCaseController useCaseController = createUseCaseController();
        ModelAndView obtained = useCaseController.getUseCaseView(1,1, 1,new ModelAndView(),principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("useCase"), expected.getModel().get("useCase"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
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
        verify(featureService,times(1)).getFeature(1);
        verify(documentService, times(1)).getFileList(1,1);
        verify(principal, times(4)).getName();
        verify(projectDom, times(1)).getRequirementTypes();
        verify(traceabilityService, times(1)).getNotTracedReqs(1,1);
        verify(traceabilityService, times(1)).getNotTracedFeatures(1,1);
        verify(traceabilityService, times(1)).getNotTracedUseCases(1,1);
        verify(traceabilityService, times(0)).getNotTracedEpics(1,1);
        verify(traceabilityService, times(0)).getNotTracedUserStories(1,1);
        verify(useCaseService, times(1)).getUseCase(1);
    }

    @Test
    @DisplayName("Request a view with a UseCase throws an AccessDeniedException")
    public void getUseCaseView_ProjectIdAndAndFeatureIdAndUseCaseProvided_ThrowsException(){
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
        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.getUseCaseView(1,1, 1,new ModelAndView(),principal);
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
        verify(featureService,times(0)).getFeature(1);
        verify(documentService, times(0)).getFileList(1,1);
        verify(principal, times(2)).getName();
        verify(projectDom, times(0)).getRequirementTypes();
        verify(traceabilityService, times(0)).getNotTracedReqs(1,1);
        verify(traceabilityService, times(0)).getNotTracedEpics(1,1);
        verify(traceabilityService, times(0)).getNotTracedUserStories(1,1);
        verify(traceabilityService, times(0)).getNotTracedFeatures(1,1);
        verify(traceabilityService, times(0)).getNotTracedUseCases(1,1);
        verify(useCaseService, times(0)).getUseCase(1);
    }

    @Test
    @DisplayName("Request a view with a createUseCase view")
    public void getCreateUseCaseView_ProjectIdAndFeatureIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        FeatureDom feature = mock(FeatureDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(featureService.getFeature(1)).thenReturn(feature);
        when(feature.getIdFeature()).thenReturn(1);
        when(feature.getName()).thenReturn("feature");

        ModelAndView expected = new ModelAndView("createFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("useCase", new UseCaseDom());
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");

        UseCaseController useCaseController = createUseCaseController();
        ModelAndView obtained = useCaseController.getCreateUseCaseView(1,1, new ModelAndView(), principal);
        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("useCase"), expected.getModel().get("useCase"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
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
        verify(featureService, times(1)).getFeature(1);
    }

    @Test
    @DisplayName("Request a view with a createUseCase view not allowed")
    public void getCreateUseCaseView_ProjectIdAndFeatureIdProvided_AccessDeniedExceptionThrown(){
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

        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.getCreateUseCaseView(1,1, new ModelAndView(), principal);
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
        verify(featureService, times(0)).getFeature(1);
    }

    @Test
    @DisplayName("Request a view with a updateUseCase view")
    public void getUpdateUseCaseView_ProjectIdProvidedAndFeatureIdAndUseCaseDomProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        FeatureDom feature = mock(FeatureDom.class);
        UseCaseDom useCase = mock(UseCaseDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.getFeature(1)).thenReturn(feature);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(useCaseService.getUseCase(1)).thenReturn(useCase);
        when(featureService.getFeature(1)).thenReturn(feature);
        when(feature.getIdFeature()).thenReturn(1);
        when(feature.getName()).thenReturn("feature");

        ModelAndView expected = new ModelAndView("updateUseCase");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("useCase", useCase);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getUpdateUseCaseView(1,1, 1,new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("useCase"), expected.getModel().get("useCase"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
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
        verify(featureService, times(1)).getFeature(1);
        verify(useCaseService, times(1)).getUseCase(1);
    }

    @Test
    @DisplayName("Request a view with a UseCase view access denied")
    public void getUpdateUseCaseView_ProjectIdAndFeatureIdAndUseCaseDomProvided_AccessDeniedExceptionThrown(){
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

        UseCaseController useCaseController = createUseCaseController();

        try {
            useCaseController.getUpdateUseCaseView(1,1, 1,new ModelAndView(), principal);
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
        verify(featureService, times(0)).getFeature(1);
        verify(useCaseService, times(0)).getUseCase(1);
    }

    @Test
    @DisplayName("createUseCase method returns a modelAndView object")
    public void createUseCase_ProjectIdAndFeatureIdAndUseCaseDomProvided_ReturnsMAV(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        UseCaseDom useCase = mock(UseCaseDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);
        when(useCaseService.create(useCase,1,1)).thenReturn(useCase);

        ModelAndView expected = new ModelAndView("redirect: /project/1/feature/1/usecase/1");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.createUseCase(1,1,principal,useCase, bindingResult);
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
        verify(featureService, times(0)).getFeature(1);
        verify(useCaseService, times(1)).create(useCase,1,1);
    }

    @Test
    @DisplayName("createFeature method returns a modelAndView object")
    public void createFeature_ProjectIdAndFeatureDomProvided_ReturnsErrorMAV(){
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        UseCaseDom useCase = mock(UseCaseDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        when(featureDom.getIdFeature()).thenReturn(1);
        when(featureDom.getName()).thenReturn("feature");

        UseCaseController useCaseController = createUseCaseController();
        ModelAndView expected = new ModelAndView("useCase");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("useCase", useCase);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = useCaseController.createUseCase(1,1, principal,useCase,result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("useCase"), expected.getModel().get("useCase"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
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
        verify(featureService, times(1)).getFeature(1);
        verify(useCaseService, times(0)).create(useCase,1,1);
    }

    @Test
    @DisplayName("createFeature method returns not allowed")
    public void createFeature_ProjectIdAndEpicDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.createUseCase(1,1,principal,useCaseDom,result);
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
        verify(featureService, times(0)).getFeature(1);
        verify(useCaseService, times(0)).create(useCaseDom,1,1);
    }

    @Test
    @DisplayName("updateUseCase method returns a modelAndView object")
    public void updateUseCase_ProjectIdAndFeatureIdAndUseCaseIdAndUseCaseDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        BindingResult result = mock(BindingResult.class);
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(false);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        when(featureDom.getIdFeature()).thenReturn(1);
        when(featureDom.getName()).thenReturn("feature");
        when(useCaseService.update(1,1,1,useCaseDom)).thenReturn(useCaseDom);

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/feature/1/usecase/1");
        ModelAndView obtained = useCaseController.updateUseCase(1,1, principal,1,useCaseDom, result);

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
        verify(featureService, times(0)).getFeature(1);
        verify(useCaseService, times(1)).update(1,1,1,useCaseDom);
    }

    @Test
    @DisplayName("updateUseCase method returns a modelAndView object")
    public void updateUseCase_ProjectIdAndUseCaseIdAndUseCaseDomProvided_ReturnsMAV(){
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        UseCaseDom useCase = mock(UseCaseDom.class);
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
        when(featureService.getFeature(1)).thenReturn(featureDom);
        when(featureDom.getIdFeature()).thenReturn(1);
        when(featureDom.getName()).thenReturn("feature");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView expected = new ModelAndView("updateFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("useCase", useCase);
        expected.addObject("featureId", 1);
        expected.addObject("featureName", "feature");
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = useCaseController.updateUseCase(1,1, principal,1,useCase, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("useCase"), expected.getModel().get("useCase"));
        assertEquals(obtained.getModel().get("featureId"), expected.getModel().get("featureId"));
        assertEquals(obtained.getModel().get("featureName"), expected.getModel().get("featureName"));
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
        verify(featureService, times(1)).getFeature(1);
        verify(useCaseService, times(0)).update(1,1,1,useCase);
    }

    @Test
    @DisplayName("updateUseCase method returns not allowed")
    public void updateUseCase_ProjectIdAndUseCaseDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        UseCaseDom useCase = mock(UseCaseDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        UseCaseController useCaseController = createUseCaseController();

        try {
            useCaseController.updateUseCase(1,1, principal,1,useCase, result);
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
        verify(featureService, times(0)).getFeature(1);
        verify(useCaseService, times(0)).update(1,1,1,useCase);
    }

    @Test
    @DisplayName("deleteUseCase method returns an http.ok when UseCase is deleted")
    public void deleteUseCase_idProjectAndFeatureIdAndUseCaseIdProvided_UseCaseDeleted_ReturnsOk(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(useCaseService.deleteUseCase(1)).thenReturn(true);

        UseCaseController useCaseController = createUseCaseController();
        ResponseEntity obtained = useCaseController.deleteUseCase(1,1, principal,1);

        //Test conditions
        assertEquals(HttpStatus.OK,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(useCaseService,times(1)).deleteUseCase(1);
    }
    @Test
    @DisplayName("deleteUseCase method returns an http.Internal_Server_Error when UseCase is not deleted")
    public void deleteUseCase_idProjectAndFeatureIdAndUseCaseIdProvided_UseCaseNotDeleted_ReturnsInternalServerError(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(useCaseService.deleteUseCase(1)).thenReturn(false);

        UseCaseController useCaseController = createUseCaseController();
        ResponseEntity obtained = useCaseController.deleteUseCase(1,1, principal,1);

        //Test conditions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(useCaseService,times(1)).deleteUseCase(1);
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

        UseCaseController useCaseController = createUseCaseController();
        try {
            useCaseController.deleteUseCase(1,1, principal,1);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(2)).getName();
        verify(featureService,times(0)).deleteFeature(1);
    }

    private UseCaseController createUseCaseController(){
        return new UseCaseController(projectService, featureService, useCaseService, documentService, commonMethods,
                userService, traceabilityService, commentService);
    }
}

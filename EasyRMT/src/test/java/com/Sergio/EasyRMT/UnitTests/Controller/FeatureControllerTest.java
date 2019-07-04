/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.FeatureController;
import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.*;
import org.junit.Assert;
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
public class FeatureControllerTest {

    @Mock
    private ProjectService projectService;
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
    @DisplayName("Request a view with a list of features")
    public void getFeatureListView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        List<FeatureDom> featureDomList = mock(List.class);
        GroupDom group = mock(GroupDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.getFeatures(anyInt())).thenReturn(featureDomList);
        when(commonMethods.isPM(user,"user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("featuresDashboard");
        expected.addObject("project", projectDom);
        expected.addObject("featureList", featureDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getFeatureListView(1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("featureList"), expected.getModel().get("featureList"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("group"), obtained.getModel().get("group"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(featureService,times(1)).getFeatures(1);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a list of Features throws an AccessDeniedException")
    public void getFeatureListView_ProjectIdProvided_ThrowsAccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        FeatureController featureController = createFeatureController();
        try {
            featureController.getFeatureListView(1, new ModelAndView(), principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }

        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList, projectDom);
        verify(featureService,times(0)).getFeatures(1);
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("Request a view with a feature")
    public void getFeatureView_ProjectIdAndFeatureIdProvided_ReturnView(){
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
        ModelAndView expected = new ModelAndView("feature");
        expected.addObject("feature", feature);
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

        FeatureController featureController = createFeatureController();
        ModelAndView obtained = featureController.getFeatureView(1,1, new ModelAndView(),principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("feature"), expected.getModel().get("feature"));
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
    }

    @Test
    @DisplayName("Request a view with a Feature throws an AccessDeniedException")
    public void getFeatureView_ProjectIdAndFeatureIdProvided_ThrowsException(){
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
        FeatureController featureController = createFeatureController();
        try {
            featureController.getFeatureView(1,1, new ModelAndView(),principal);
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
    }

    @Test
    @DisplayName("Request a view with a createFeature view")
    public void getCreateFeatureView_ProjectIdProvided_ReturnView(){
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

        ModelAndView expected = new ModelAndView("createFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", new FeatureDom());
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        FeatureController featureController = createFeatureController();
        ModelAndView obtained = featureController.getCreateFeatureView(1, new ModelAndView(), principal);
        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("feature"), expected.getModel().get("feature"));
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
    @DisplayName("Request a view with a createFeature view not allowed")
    public void getCreateFeatureView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        FeatureController featureController = createFeatureController();

        try {
            featureController.getCreateFeatureView(1, new ModelAndView(), principal);
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
    @DisplayName("Request a view with a updateFeature view")
    public void getUpdateFeatureView_ProjectIdProvided_ReturnView(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        GroupDom group = mock(GroupDom.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        List<Group_user> users = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        ModelAndView expected = new ModelAndView("updateFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("priority", Priority.values());
        expected.addObject("state", State.values());
        expected.addObject("risk", Risk.values());
        expected.addObject("complexity", Complexity.values());
        expected.addObject("scope", Scope.values());
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getUpdateFeatureView(1,1, new ModelAndView(), principal);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("feature"), expected.getModel().get("feature"));
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
    @DisplayName("Request a view with a updateFeature view access denied")
    public void getUpdateFeatureView_ProjectIdProvided_AccessDeniedExceptionThrown(){
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

        FeatureController featureController = createFeatureController();

        try {
            featureController.getUpdateFeatureView(1,1, new ModelAndView(), principal);
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
    @DisplayName("createFeature method returns a modelAndView redirect")
    public void createFeature_ProjectIdAndFeatureDomProvided_ReturnsRedirect(){
        FeatureDom featureDom = mock(FeatureDom.class);
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
        when(featureService.create(featureDom,1)).thenReturn(featureDom);
        when(featureDom.getIdFeature()).thenReturn(1);

        ModelAndView expected = new ModelAndView("redirect: /project/1/feature/1");

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.createFeature(1,featureDom,principal,bindingResult);
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
        verify(featureService, times(1)).create(featureDom, 1);
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
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(projectDom.getGroup()).thenReturn(group);
        when(group.getUsers()).thenReturn(users);

        FeatureController featureController = createFeatureController();
        ModelAndView expected = new ModelAndView("createFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = featureController.createFeature(1,featureDom, principal,result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("feature"), expected.getModel().get("feature"));
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
        verify(featureService, times(0)).create(featureDom, 1);
    }

    @Test
    @DisplayName("createFeature method returns not allowed")
    public void createFeature_ProjectIdAndEpicDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        FeatureController featureController = createFeatureController();
        try {
            featureController.createFeature(1,featureDom, principal,result);
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
        verify(featureService, times(0)).create(featureDom, 1);
    }

    @Test
    @DisplayName("updateFeature method returns a modelAndView redirect")
    public void updateFeature_ProjectIdAndFeatureIdAndFeatureDomProvided_ReturnsRedirect(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
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
        when(featureService.update(featureDom,1,1)).thenReturn(featureDom);
        when(featureDom.getIdFeature()).thenReturn(1);

        FeatureController featureController = createFeatureController();

        ModelAndView expected = new ModelAndView("redirect: /project/1/feature/1");
        ModelAndView obtained = featureController.updateFeature(1,1, featureDom, principal, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        Assert.assertTrue(obtained.getModel().isEmpty());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectDom, times(0)).getGroup();
        verify(principal, times(1)).getName();
        verify(featureService, times(1)).update(featureDom, 1,1);
    }

    @Test
    @DisplayName("updateFeature method returns a modelAndView object")
    public void updateFeature_ProjectIdAndFeatureIdAndFeatureDomProvided_ReturnsMAV(){
        FeatureDom featureDom = mock(FeatureDom.class);
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

        FeatureController featureController = createFeatureController();

        ModelAndView expected = new ModelAndView("updateFeature");
        expected.addObject("project", projectDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("user", "user");
        expected.addObject("group", users);
        expected.addObject("isPM", true);

        ModelAndView obtained = featureController.updateFeature(1,1, featureDom, principal, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("feature"), expected.getModel().get("feature"));
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
        verify(featureService, times(0)).update(featureDom, 1,1);
    }

    @Test
    @DisplayName("updateFeature method returns not allowed")
    public void updateFeature_ProjectIdAndFeatureDomProvided_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        BindingResult result = mock(BindingResult.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        FeatureController featureController = createFeatureController();

        try {
            featureController.updateFeature(1,1, featureDom, principal, result);
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
        verify(featureService, times(0)).update(featureDom, 1,1);
    }

    @Test
    @DisplayName("deleteFeature method returns an http.ok when efeature is deleted")
    public void deleteFeature_idProjectAndFeatureIdProvided_FeatureDeleted_ReturnsOk(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.deleteFeature(1)).thenReturn(true);

        FeatureController featureController = createFeatureController();
        ResponseEntity obtained = featureController.deleteFeature(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.OK,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(featureService,times(1)).deleteFeature(1);
    }

    @Test
    @DisplayName("deleteFeature method returns an http.Internal_Server_Error when feature is not deleted")
    public void deleteFeature_idProjectAndFeatureIdProvided_FeatureNotDeleted_ReturnsInternalServerError(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(featureService.deleteFeature(1)).thenReturn(false);

        FeatureController featureController = createFeatureController();
        ResponseEntity obtained = featureController.deleteFeature(1,1, principal);

        //Test conditions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,obtained.getStatusCode());
        assertEquals("", obtained.getBody());
        verify(projectService,times(1)).getProject(1);
        verify(userService,times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(principal, times(1)).getName();
        verify(featureService,times(1)).deleteFeature(1);
    }

    @Test
    @DisplayName("deleteFeature method throws exception when Feature is not deleted")
    public void deleteFeature_idProjectAndFeatureIdProvided_FeatureNotDeleted_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        FeatureController featureController = createFeatureController();
        try {
            featureController.deleteFeature(1,1, principal);
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

    private FeatureController createFeatureController(){
        return new FeatureController(projectService, featureService, documentService, commonMethods, userService,
                traceabilityService, commentService);
    }
}

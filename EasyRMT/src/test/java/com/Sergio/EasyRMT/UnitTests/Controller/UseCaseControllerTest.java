/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.UseCaseController;
import com.Sergio.EasyRMT.Controller.UserStoryController;
import com.Sergio.EasyRMT.Domain.*;
import com.Sergio.EasyRMT.Service.*;
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
public class UseCaseControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    UseCaseService useCaseService;
    @Mock
    FeatureService featureService;
    @Mock
    DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        useCaseService = mock(UseCaseService.class);
        featureService = mock(FeatureService.class);
        documentService = mock(DocumentService.class);
    }

    @Test
    @DisplayName("Request a view with a list of UseCases")
    public void getUseCaseListView_ProjectIdAndFeatureIdProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        FeatureDom featureDom =  mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(useCaseService.getUseCases(anyInt())).thenReturn(useCaseDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("useCasesDashboard");
        expected.addObject("project", project);
        expected.addObject("useCasesList", useCaseDomList);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getUserCasesListView(19,1, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCasesList").equals(expected.getModel().get("useCasesList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).getUseCases(1);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("Request a view with a list of UseCases")
    public void getUseCaseListView_ProjectIdProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(useCaseService.getByProjectID(anyInt())).thenReturn(useCaseDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("useCasesDashboardProject");
        expected.addObject("project", project);
        expected.addObject("useCasesList", useCaseDomList);
        expected.addObject("projectList", projectDomList);

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getUserCasesListView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCasesList").equals(expected.getModel().get("useCasesList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).getByProjectID(19);*/
    }

    @Test
    @DisplayName("Request a view with an UseCase")
    public void getUseCaseView_ProjectIdAndFeatureIdAndUseCaseIdProvided_ReturnView(){
       /* List<ProjectDom> projectDomList = mock(List.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        FeatureDom featureDom =  mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(useCaseService.getUseCase(anyInt())).thenReturn(useCaseDom);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("useCase");
        expected.addObject("project", project);
        expected.addObject("useCase", useCaseDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getUseCaseView(19,1, 1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCase").equals(expected.getModel().get("useCase")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).getUseCase(1);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("Request a view with a createUseCase view")
    public void getCreateUseCaseView_ProjectIdAndFeatureIdProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        UseCaseDom useCaseDom = new UseCaseDom();
        ProjectDom project = mock(ProjectDom.class);
        FeatureDom featureDom =  mock(FeatureDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("createUseCase");
        expected.addObject("project", project);
        expected.addObject("useCase", useCaseDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getCreateUseCaseView(19,1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCase").equals(expected.getModel().get("useCase")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("Request a view with a updateUseCase view")
    public void getUpdateUseCaseView_ProjectIdProvidedAndFeatureIdAndUseCaseDomProvided_ReturnView(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        ProjectDom project = mock(ProjectDom.class);
        FeatureDom featureDom =  mock(FeatureDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(useCaseService.getUseCase(anyInt())).thenReturn(useCaseDom);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("updateUseCase");
        expected.addObject("project", project);
        expected.addObject("useCase", useCaseDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.getUpdateUseCaseView(19,1,1,new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCase").equals(expected.getModel().get("useCase")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).getUseCase(1);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("createUseCase method returns a modelAndView object")
    public void createUseCase_ProjectIdAndFeatureIdAndUseCaseDomProvided_ReturnsMAV(){
        /*List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(useCaseService.create(useCaseDom,1, 19)).thenReturn(useCaseDom);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("useCase");
        expected.addObject("project", project);
        expected.addObject("useCase", useCaseDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.createUseCase(19,1, useCaseDom);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCase").equals(expected.getModel().get("useCase")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).create(useCaseDom,1,19);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("updateUseCase method returns a modelAndView object")
    public void updateUseCase_ProjectIdAndFeatureIdAndUseCaseIdAndUseCaseDomProvided_ReturnsMAV(){
       /* List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        UseCaseDom useCaseDom = mock(UseCaseDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(useCaseService.update(19,1, 1,useCaseDom)).thenReturn(useCaseDom);
        when(featureService.getFeature(1)).thenReturn(featureDom);
        doReturn("featureName").when(featureDom).getName();
        doReturn(1).when(featureDom).getIdFeature();

        ModelAndView expected = new ModelAndView("useCase");
        expected.addObject("project", project);
        expected.addObject("useCase", useCaseDom);
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureId",1);
        expected.addObject("featureName", "featureName");

        UseCaseController useCaseController = createUseCaseController();

        ModelAndView obtained = useCaseController.updateUseCase(19,1,1, useCaseDom);

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("useCase").equals(expected.getModel().get("useCase")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("featureId").equals(expected.getModel().get("featureId")));
        assertTrue(obtained.getModel().get("featureName").equals(expected.getModel().get("featureName")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify userStory is called 1 time
        verify(useCaseService,times(1)).update(19,1,1,useCaseDom);
        //verify epic service has been called
        verify(featureService, times(1)).getFeature(1);
        //verify featureDom has been called
        verify(featureDom, times(1)).getIdFeature();
        verify(featureDom, times(1)).getName();*/
    }

    @Test
    @DisplayName("deleteUseCase method returns an http.ok when UseCase is deleted")
    public void deleteUseCase_idProjectAndFeatureIdAndUseCaseIdProvided_UseCaseDeleted_ReturnsOk(){
        when(useCaseService.deleteUseCase(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        UseCaseController useCaseController = createUseCaseController();

        ResponseEntity obtained = useCaseController.deleteUseCase(7,1,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(useCaseService,times(1)).deleteUseCase(1);
    }
    @Test
    @DisplayName("deleteUseCase method returns an http.Internal_Server_Error when UseCase is not deleted")
    public void deleteUseCase_idProjectAndFeatureIdAndUseCaseIdProvided_UseCaseNotDeleted_ReturnsInternalServerError(){
        when(useCaseService.deleteUseCase(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        UseCaseController useCaseController = createUseCaseController();

        ResponseEntity obtained = useCaseController.deleteUseCase(7,1,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(useCaseService,times(1)).deleteUseCase(1);
    }

    private UseCaseController createUseCaseController(){
        return new UseCaseController(projectService, featureService, useCaseService, documentService);
    }
}

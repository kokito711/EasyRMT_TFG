/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.EpicController;
import com.Sergio.EasyRMT.Controller.FeatureController;
import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.EpicService;
import com.Sergio.EasyRMT.Service.FeatureService;
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
public class FeatureControllerTest {

    @Mock
    ProjectService projectService;
    @Mock
    FeatureService featureService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        featureService = mock(FeatureService.class);
    }

    @Test
    @DisplayName("Request a view with a list of features")
    public void getFeatureListView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        List<FeatureDom> featureDomList = mock(List.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(featureService.getFeatures(anyInt())).thenReturn(featureDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("featuresDashboard");
        expected.addObject("projectList", projectDomList);
        expected.addObject("featureList", featureDomList);
        expected.addObject("project", project);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getFeatureListView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("featureList").equals(expected.getModel().get("featureList")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify getEpics is called 1 time
        verify(featureService,times(1)).getFeatures(19);

    }

    @Test
    @DisplayName("Request a view with a feature")
    public void getFeatureView_ProjectIdAndFeatureIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(featureService.getFeature(anyInt())).thenReturn(featureDom);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("feature");
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("project", project);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getFeatureView(19,19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("feature").equals(expected.getModel().get("feature")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
        //verify getEpics is called 1 time
        verify(featureService,times(1)).getFeature(19);
    }

    @Test
    @DisplayName("Request a view with a createFeature view")
    public void getCreateFeatureView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = new FeatureDom();
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);

        ModelAndView expected = new ModelAndView("createFeature");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getCreateFeatureView(19, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("feature").equals(expected.getModel().get("feature")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        verify(projectService,times(1)).getProject(19);
    }

    @Test
    @DisplayName("Request a view with a updateFeature view")
    public void getUpdateFeatureView_ProjectIdProvided_ReturnView(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.getFeature(anyInt())).thenReturn(featureDom);

        ModelAndView expected = new ModelAndView("updateFeature");
        expected.addObject("project", project);
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.getUpdateFeatureView(19,1, new ModelAndView());

        //TestConditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("feature").equals(expected.getModel().get("feature")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
        //verify getReqs is called 1 time
        verify(featureService,times(1)).getFeature(1);
    }

    @Test
    @DisplayName("createFeature method returns a modelAndView object")
    public void createFeature_ProjectIdAndFeatureDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.create(featureDom, 17)).thenReturn(featureDom);

        ModelAndView expected = new ModelAndView("feature");
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("project", project);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.createFeature(17,featureDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("feature").equals(expected.getModel().get("feature")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(featureService,times(1)).create(featureDom,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
    }

    @Test
    @DisplayName("updateFeature method returns a modelAndView object")
    public void updateFeature_ProjectIdAndFeatureIdAndFeatureDomProvided_ReturnsMAV(){
        List<ProjectDom> projectDomList = mock(List.class);
        FeatureDom featureDom = mock(FeatureDom.class);
        ProjectDom project = mock(ProjectDom.class);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(anyInt())).thenReturn(project);
        when(featureService.update(featureDom,1, 17)).thenReturn(featureDom);

        ModelAndView expected = new ModelAndView("feature");
        expected.addObject("projectList", projectDomList);
        expected.addObject("feature", featureDom);
        expected.addObject("project", project);

        FeatureController featureController = createFeatureController();

        ModelAndView obtained = featureController.updateFeature(17,1,featureDom);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("feature").equals(expected.getModel().get("feature")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //verify getReqs is called 1 time
        verify(featureService,times(1)).update(featureDom,1,17);
        //Verify  getProject has been called
        verify(projectService, times(1)).getProject(anyInt());
    }

    @Test
    @DisplayName("deleteFeature method returns an http.ok when efeature is deleted")
    public void deleteFeature_idProjectAndFeatureIdProvided_FeatureDeleted_ReturnsOk(){
        when(featureService.deleteFeature(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        FeatureController featureController = createFeatureController();

        ResponseEntity obtained = featureController.deleteFeature(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(featureService,times(1)).deleteFeature(1);
    }
    @Test
    @DisplayName("deleteFeature method returns an http.Internal_Server_Error when feature is not deleted")
    public void deleteEpic_idProjectAndEpicIdProvided_EpicDeleted_ReturnsInternalServerError(){
        when(featureService.deleteFeature(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        FeatureController featureController = createFeatureController();

        ResponseEntity obtained = featureController.deleteFeature(7,1);

        //Test conditions
        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        //Verify project service has been called
        verify(featureService,times(1)).deleteFeature(1);
    }

    private FeatureController createFeatureController(){
        return new FeatureController(projectService, featureService);
    }
}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.FeatureRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.FeatureConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.FeatureService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class FeatureServiceTest {

    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private FeatureRepository featureRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private FeatureConverter featureConverter;
    @Mock
    private DocumentService documentService;
    @Mock
    private UserRepository userRepository;

    private Date date;

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        featureRepository = mock(FeatureRepository.class);
        projectRepository = mock(ProjectRepository.class);
        featureConverter = mock(FeatureConverter.class);
        documentService = mock(DocumentService.class);
        userRepository = mock(UserRepository.class);
        date = new Date();
    }

    @Test
    @DisplayName("Get a list of features")
    public void getListFeature_ProjectIdProvided_ListReturned(){
        List<Feature> featureList = mock(List.class);
        List<FeatureDom> featureDomList = mock(List.class);

        when(featureRepository.findByProjectID(anyInt())).thenReturn(featureList);
        when(featureConverter.toDomain(featureList)).thenReturn(featureDomList);

        FeatureService featureService = createFeatureService();
        //Test conditions
        assertTrue(featureService.getFeatures(7).equals(featureDomList));
        //Verify featureRepository has been called one time
        verify(featureRepository,times(1)).findByProjectID(7);
        //Verify featureConverter has been called one time
        verify(featureConverter,times(1)).toDomain(featureList);
    }

    @Test
    @DisplayName("Get an feature")
    public void getFeature_FeatureIdProvided_FeatureReturned(){
        Feature feature = mock(Feature.class);
        FeatureDom featureDom = mock(FeatureDom.class);

        when(featureRepository.findOne(1)).thenReturn(feature);
        when(featureConverter.toDomain(feature)).thenReturn(featureDom);

        FeatureService featureService = createFeatureService();
        FeatureDom obtained = featureService.getFeature(1);
        //Test conditions
        assertEquals(obtained, featureDom);
        //Verify featureRepository has been called one time
        verify(featureRepository,times(1)).findOne(1);
        //Verify featureConverter has been called one time
        verify(featureConverter,times(1)).toDomain(feature);
    }

    @Test
    @DisplayName("Create feature persists an feature from an feature provided")
    public void createFeature_FeatureProvided_ReturnsFeaturePersisted(){
        FeatureDom featureDom = createFeatureDom();
        Feature feature = createFeature();
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        User user = feature.getAssignedTo();
        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(featureConverter.toModel(featureDom)).thenReturn(feature);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(featureRepository.save(feature)).thenReturn(feature);
        when(featureConverter.toDomain(feature)).thenReturn(featureDom);
        when(userRepository.findOne(featureDom.getAuthorId())).thenReturn(user);
        when(userRepository.findOne(featureDom.getAssignedId())).thenReturn(user);

        FeatureService featureService = createFeatureService();
        FeatureDom obtained = featureService.create(featureDom,7);

        //Test conditions
        assertEquals(obtained,featureDom);
        assertNotNull(obtained);
        verify(featureConverter, times(1)).toModel(featureDom);
        verify(projectRepository, times(1)).findByIdProject(7);
        verify(featureConverter, times(1)).toDomain(feature);
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        verify(featureRepository, times(1)).save(feature);
        verify(objectEntity,times(1)).getIdobject();
        verify(userRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("DeleteFeature deletes an feature and all its children")
    public void deleteFeature_IdObjectProvided_DeletesFeatureObjectAndChildren(){
        Feature feature = createFeature();
        List<UseCase> useCases = new ArrayList<>();
        UseCase useCase = mock(UseCase.class);
        useCases.add(useCase);
        feature.setUseCases(useCases);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(useCase.getObject()).thenReturn(objectEntity);
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(objectRepository).deleteObject(anyInt());
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doReturn(false).when(featureRepository).exists(anyInt());
        when(featureRepository.findOne(anyInt())).thenReturn(feature);

        FeatureService featureService = createFeatureService();

        assertTrue(featureService.deleteFeature(7));
        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(2)).deleteObject(anyInt());
        verify(featureRepository,times(1)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("deleteFeature fails to delete a feature")
    public void deleteFeature_IdObjectProvided_DeletionFailed(){
        Feature feature = mock(Feature.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(true).when(objectRepository).exists(anyInt());
        when(feature.getObject()).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(featureRepository).exists(anyInt());
        when(featureRepository.findOne(anyInt())).thenReturn(feature);

        FeatureService featureService = createFeatureService();

        assertFalse(featureService.deleteFeature(7));
        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(featureRepository,times(0)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("deleteFeature fails when feature does not exists in DB")
    public void deleteFeature_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        FeatureService featureService = createFeatureService();

        //Test conditions
        assertFalse(featureService.deleteFeature(7));
        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).deleteObject(anyInt());
        verify(featureRepository,times(0)).exists(anyInt());
        verify(documentService, times(0)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Update feature persist infomation")
    public void updateFeature_FeatureModified_FeatureUpdated(){
        FeatureDom featureDom = new FeatureDom();
        UserDom userDom2 = mock(UserDom.class);
        featureDom.setIdFeature(1);
        featureDom.setName("Test1");
        featureDom.setIdentifier("12345");
        featureDom.setDescription("description1");
        featureDom.setPriority(Priority.BLOCKER);
        featureDom.setComplexity(Complexity.HIGH);
        featureDom.setState(State.APPROVED);
        featureDom.setCost(27.0);
        featureDom.setEstimatedHours(29.00);
        featureDom.setStoryPoints(5);
        featureDom.setSource("source1");
        featureDom.setScope(Scope.PROJECT);
        featureDom.setRisk(Risk.LOW);
        featureDom.setCreated(date);
        featureDom.setLastUpdated(date);
        featureDom.setVersion("version1");
        featureDom.setValidationMethod("validation1");
        featureDom.setAuthor(userDom2);
        featureDom.setAssignedTo(userDom2);
        featureDom.setJustification("justification1");
        featureDom.setTestCases("test cases1");
        featureDom.setProjectId(1);
        featureDom.setAssignedId(1);

        Feature feature = createFeature();
        User user = feature.getAssignedTo();
        when(featureRepository.findOne(1)).thenReturn(feature);
        when(userRepository.findOne(featureDom.getAssignedId())).thenReturn(user);
        when(user.getUserId()).thenReturn(5);
        when(featureRepository.save(any(Feature.class))).thenReturn(feature);
        when(featureConverter.toDomain(any(Feature.class))).thenReturn(featureDom);

        FeatureService featureService = createFeatureService();
        FeatureDom obtained = featureService.update(featureDom,1,1);

        assertEquals(obtained,featureDom);
        assertNotNull(obtained);
        verify(featureRepository,times(1)).findOne(anyInt());
        verify(featureRepository,times(1)).save(any(Feature.class));
        verify(featureConverter, times(1)).toDomain(any(Feature.class));
        verify(userRepository, times(1)).findOne(anyInt());

    }

    private FeatureService createFeatureService(){
        return new FeatureService(objectRepository,featureRepository,projectRepository,featureConverter,
                documentService, userRepository);
    }

    private FeatureDom createFeatureDom(){
        FeatureDom featureDom = new FeatureDom();
        featureDom.setIdFeature(1);
        featureDom.setName("Test");
        featureDom.setIdentifier("1234");
        featureDom.setDescription("description");
        featureDom.setPriority(Priority.NORMAL);
        featureDom.setComplexity(Complexity.NORMAL);
        featureDom.setState(State.DRAFT);
        featureDom.setCost(0.0);
        featureDom.setEstimatedHours(27.00);
        featureDom.setStoryPoints(0);
        featureDom.setSource("source");
        featureDom.setScope(Scope.FEATURE);
        featureDom.setRisk(Risk.HIGH);
        featureDom.setCreated(date);
        featureDom.setLastUpdated(date);
        featureDom.setVersion("version");
        featureDom.setValidationMethod("validation");
        featureDom.setJustification("justification");
        featureDom.setTestCases("test cases");
        featureDom.setProjectId(1);

        UserDom userDom = mock(UserDom.class);
        featureDom.setAssignedTo(userDom);
        featureDom.setAuthor(userDom);
        return featureDom;
    }

    private Feature createFeature(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        Feature feature = new Feature();
        feature.setIdFeature(1);
        feature.setName("Test");
        feature.setIdentifier("1234");
        feature.setDescription("description");
        feature.setPriority(Priority.NORMAL);
        feature.setComplexity(Complexity.NORMAL);
        feature.setState(State.DRAFT);
        feature.setCost(0.0);
        feature.setEstimatedHours(27.00);
        feature.setStoryPoints(0);
        feature.setSource("source");
        feature.setScope(Scope.FEATURE);
        feature.setRisk(Risk.HIGH);
        feature.setCreated(date);
        feature.setLastUpdated(date);
        feature.setVersion("version");
        feature.setValidationMethod("validation");
        feature.setJustification("justification");
        feature.setTestCases("test cases");
        feature.setObject(objectEntity);

        User user = mock(User.class);
        feature.setAssignedTo(user);
        feature.setAuthor(user);
        return feature;
    }
}

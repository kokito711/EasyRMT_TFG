/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Repository.FeatureRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        featureRepository = mock(FeatureRepository.class);
        projectRepository = mock(ProjectRepository.class);
        featureConverter = mock(FeatureConverter.class);
        documentService = mock(DocumentService.class);
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

        when(featureRepository.findOne(anyInt())).thenReturn(feature);
        when(featureConverter.toDomain(feature)).thenReturn(featureDom);

        FeatureService featureService = createFeatureService();
        //Test conditions
        assertTrue(featureService.getFeature(7).equals(featureDom));
        //Verify featureRepository has been called one time
        verify(featureRepository,times(1)).findOne(7);
        //Verify featureConverter has been called one time
        verify(featureConverter,times(1)).toDomain(feature);
    }

    @Test
    @DisplayName("Create feature persists an feature from an feature provided")
    public void createFeature_FeatureProvided_ReturnsFeaturePersisted(){
        FeatureDom featureDom = mock(FeatureDom.class);
        Feature feature = mock(Feature.class);
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);

        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(featureConverter.toModel(featureDom)).thenReturn(feature);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(featureRepository.save(feature)).thenReturn(feature);
        when(featureConverter.toDomain(feature)).thenReturn(featureDom);


        FeatureService featureService = createFeatureService();

        //Test conditions
        assertEquals(featureService.create(featureDom,7),featureDom);
        //Verify that featureConverter.toModel has been called once
        verify(featureConverter, times(1)).toModel(featureDom);
        //verify that projectRepository.save has been called once
        verify(projectRepository, times(1)).findByIdProject(7);
        //verify that featureConverter.toDomain has been called once
        verify(featureConverter, times(1)).toDomain(feature);
        //verify objectRepository has been called
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        //verify feature repository has been called
        verify(featureRepository, times(1)).save(feature);
        //verify feature.setIdeFeature has been called
        verify(feature, times(1)).setIdFeature(9);
        //verify object entity. get id object has been called
        verify(objectEntity,times(1)).getIdobject();
        //verify feature.setAuthor has been called
        verify(featureDom, times(1)).setAuthor(anyInt());
        //verify setUserStories has been called
        verify(feature, times(1)).setUseCases(anyList());
        //Verify feature created has been called
        verify(featureDom, times(1)).setCreated(any(Date.class));
        //Verify feature last updated has been called
        verify(featureDom, times(1)).setLastUpdated(any(Date.class));
    }

    @Test
    @DisplayName("DeleteFeature deletes an feature and all its children")
    public void deleteFeature_IdObjectProvided_DeletesFeatureObjectAndChildren(){
        Feature feature = mock(Feature.class);
        List<UseCase> useCases = new ArrayList<>();
        UseCase us1 = new UseCase();
        us1.setIdUseCase(1);
        useCases.add(us1);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(feature.getUseCases()).thenReturn(useCases);
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(featureRepository).exists(anyInt());
        when(featureRepository.findOne(anyInt())).thenReturn(feature);

        FeatureService featureService = createFeatureService();

        assertTrue(featureService.deleteFeature(7));
        //Verify that method calls two times to project repository.existsByIdProject
        verify(objectRepository, times(2)).exists(anyInt());
        //Verify that projectRepository.deleteProjectByIdProject() is called once
        verify(objectRepository, times(2)).deleteObject(anyInt());
        //verify feature repository has been called
        verify(featureRepository, times(1)).findOne(anyInt());
        verify(featureRepository,times(1)).exists(anyInt());
        //verify feature has been called
        verify(feature, times(2)).getUseCases();
    }

    @Test
    @DisplayName("Delete Feature fails when feature does not exists in DB")
    public void deleteFeature_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        FeatureService featureService = createFeatureService();

        //Test conditions
        assertFalse(featureService.deleteFeature(7));
        //Verify that method calls one time to object repository.exists
        verify(objectRepository, times(1)).exists(anyInt());
        //Verify that featureRepository.findOne() is never called
        verify(featureRepository, times(0)).findOne(anyInt());
        //Verify that objectRepository.deleteObject() is never called
        verify(objectRepository, times(0)).deleteObject(anyInt());
    }

    @Test
    @DisplayName("Update feature persist infomation")
    public void updateFeature_FeatureModified_FeatureUpdated(){


    }

    private FeatureService createFeatureService(){
        return new FeatureService(objectRepository,featureRepository,projectRepository,featureConverter, documentService);
    }

}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.FeatureDom;
import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.FeatureConverter;
import com.Sergio.EasyRMT.Service.Converter.UseCaseConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringJUnit4ClassRunner.class)
public class FeatureConverterTest {
    @Mock
    UseCaseConverter useCaseConverter;
    @Mock
    UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        useCaseConverter = mock(UseCaseConverter.class);
        userConverter = mock(UserConverter.class);
    }

    @Test
    @Description("Method toDomain receives a list of Features and returns a list of FeatureDoms")
    public void toDomain_FeatureListProvided_FeatureDomListReturned(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        List<UseCase> useCaseList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);
        Feature feature = new Feature();
        feature.setIdFeature(1);
        feature.setName("FeatureTest");
        feature.setIdentifier("1234");
        feature.setAuthor(user);
        feature.setAssignedTo(user);
        feature.setObject(objectEntity);
        feature.setUseCases(useCaseList);

        when(useCaseConverter.toDomain(useCaseList)).thenReturn(useCaseDomList);

        List<Feature> featureList = new ArrayList<>();
        featureList.add(feature);

        FeatureConverter featureConverter = createFeatureConverter();

        List<FeatureDom> featureDomList = featureConverter.toDomain(featureList);

        //TestConditions
        assertNotNull(featureDomList);
        assertTrue(featureDomList.toArray().length==1);
        assertFalse(featureDomList.isEmpty());
        //Verify userStoryConverter has been called
        verify(useCaseConverter, times(1)).toDomain(useCaseList);
        verify(userConverter,times(2)).toDomain(user);

    }

    @Test
    @Description("Method toDomain receives a feature and returns an FeatureDom")
    public void toDomain_FeatureProvided_FeatureDomReturned() {
        Feature feature = new Feature();
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = mock(Project.class);
        List<UseCase> useCaseList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);

        feature.setIdFeature(1);
        feature.setName("EpicTest");
        feature.setIdentifier("Epic-1");
        feature.setDescription("Description");
        feature.setPriority(Priority.NORMAL);
        feature.setComplexity(Complexity.NORMAL);
        feature.setState(State.DRAFT);
        feature.setCost(null);
        feature.setEstimatedHours(null);
        feature.setStoryPoints(null);
        feature.setSource("Source");
        feature.setScope(Scope.PROJECT);
        feature.setRisk(Risk.HIGH);
        feature.setCreated(new Date());
        feature.setLastUpdated(new Date());
        feature.setVersion("001");
        feature.setValidationMethod("ValidationMethod");
        feature.setJustification("Justification");
        feature.setTestCases("TestCases");
        feature.setAuthor(user);
        feature.setAssignedTo(user);
        feature.setObject(objectEntity);
        feature.setUseCases(useCaseList);

        objectEntity.setIdobject(feature.getIdFeature());
        objectEntity.setProject(project);

        when(project.getIdProject()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);
        when(useCaseConverter.toDomain(useCaseList)).thenReturn(useCaseDomList);

        FeatureConverter featureConverter = createFeatureConverter();

        FeatureDom obtained = featureConverter.toDomain(feature);

        assertNotNull(obtained);
        assertEquals(obtained.getProjectId(), 1);
        assertEquals(obtained.getIdFeature(), feature.getIdFeature());
        assertEquals(obtained.getName(), feature.getName());
        assertEquals(obtained.getIdentifier(), feature.getIdentifier());
        assertEquals(obtained.getDescription(), feature.getDescription());
        assertEquals(obtained.getPriority(), feature.getPriority());
        assertEquals(obtained.getComplexity(), feature.getComplexity());
        assertEquals(obtained.getState(), feature.getState());
        assertEquals(obtained.getSource(), feature.getSource());
        assertEquals(obtained.getScope(), feature.getScope());
        assertEquals(obtained.getRisk(), feature.getRisk());
        assertEquals(obtained.getCreated(), feature.getCreated());
        assertEquals(obtained.getLastUpdated(), feature.getLastUpdated());
        assertEquals(obtained.getVersion(), feature.getVersion());
        assertEquals(obtained.getJustification(), feature.getJustification());
        assertEquals(obtained.getTestCases(), feature.getTestCases());
        //Verify userStoryConverter has been called
        verify(useCaseConverter, Mockito.times(1)).toDomain(useCaseList);
        verify(userConverter, Mockito.times(2)).toDomain(user);
    }

    @Test
    @Description("Method toDomain receives a FeatureDom and returns a Feature")
    public void toModel_FeatureDomProvided_FeatureReturned() {
        FeatureDom featureDom = new FeatureDom();
        featureDom.setIdFeature(1);
        featureDom.setName("EpicTest");
        featureDom.setIdentifier("Epic-1");
        featureDom.setDescription("Description");
        featureDom.setPriority(Priority.NORMAL);
        featureDom.setComplexity(Complexity.NORMAL);
        featureDom.setState(State.DRAFT);
        featureDom.setCost(null);
        featureDom.setEstimatedHours(0.0);
        featureDom.setStoryPoints(0);
        featureDom.setSource("Source");
        featureDom.setScope(Scope.PROJECT);
        featureDom.setRisk(Risk.HIGH);
        featureDom.setCreated(new Date());
        featureDom.setLastUpdated(new Date());
        featureDom.setVersion("001");
        featureDom.setValidationMethod("ValidationMethod");
        featureDom.setJustification("Justification");
        featureDom.setTestCases("TestCases");

        FeatureConverter featureConverter = createFeatureConverter();

        Feature obtained = featureConverter.toModel(featureDom);

        //asserts
        assertNotNull(obtained);
        assertEquals(obtained.getObject().getIdobject(), 1);
        assertNotNull(obtained.getObject().getTraced());
        Assert.assertTrue(obtained.getObject().getTraced().isEmpty());
        assertNull(obtained.getObject().getProject());
        assertEquals(obtained.getIdFeature(), featureDom.getIdFeature());
        assertEquals(obtained.getName(), featureDom.getName());
        assertEquals(obtained.getIdentifier(), featureDom.getIdentifier());
        assertEquals(obtained.getDescription(), featureDom.getDescription());
        assertEquals(obtained.getPriority(), featureDom.getPriority());
        assertEquals(obtained.getComplexity(), featureDom.getComplexity());
        assertEquals(obtained.getState(), featureDom.getState());
        assertEquals(obtained.getSource(), featureDom.getSource());
        assertEquals(obtained.getScope(), featureDom.getScope());
        assertEquals(obtained.getRisk(), featureDom.getRisk());
        assertEquals(obtained.getCreated(), featureDom.getCreated());
        assertEquals(obtained.getLastUpdated(), featureDom.getLastUpdated());
        assertEquals(obtained.getVersion(), featureDom.getVersion());
        assertEquals(obtained.getJustification(), featureDom.getJustification());
        assertEquals(obtained.getTestCases(), featureDom.getTestCases());
    }

    private FeatureConverter createFeatureConverter(){
        return new FeatureConverter(useCaseConverter, userConverter);
    }
}

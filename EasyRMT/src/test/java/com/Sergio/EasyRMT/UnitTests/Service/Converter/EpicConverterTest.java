/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicConverterTest {

    @Mock
    UserStoryConverter userStoryConverter;
    @Mock
    UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        userStoryConverter = mock(UserStoryConverter.class);
        userConverter = mock(UserConverter.class);
    }

    @Test
    @Description("Method toDomain receives a list of Epic and returns a list of EpicDom")
    public void toDomain_EpicListProvided_EpicDomListReturned(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        List<UserStory> userStoryList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        Epic epic = new Epic();
        epic.setIdEpic(1);
        epic.setName("EpicTest");
        epic.setIdentifier("1234");
        epic.setAuthor(user);
        epic.setAssignedTo(user);
        epic.setObject(objectEntity);
        epic.setUserStories(userStoryList);

        when(userStoryConverter.toDomain(userStoryList)).thenReturn(userStoryDomList);

        List<Epic> epicList = new ArrayList<>();
        epicList.add(epic);

        EpicConverter epicConverter = createEpicConverter();

        List<EpicDom> epicDomList = epicConverter.toDomain(epicList);

        //TestConditions
        assertNotNull(epicDomList);
        assertTrue(epicDomList.toArray().length==1);
        assertFalse(epicDomList.isEmpty());
        //Verify userStoryConverter has been called
        verify(userStoryConverter, times(1)).toDomain(userStoryList);
        verify(userConverter,times(2)).toDomain(user);

    }

    @Test
    @Description("Method toDomain receives an Epic and returns an EpicDom")
    public void toDomain_EpicProvided_EpicDomReturned() {
        Epic epic = new Epic();
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = mock(Project.class);
        List<UserStory> userStoryList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);

        epic.setIdEpic(1);
        epic.setName("EpicTest");
        epic.setIdentifier("Epic-1");
        epic.setDescription("Description");
        epic.setDefinitionOfDone("DoD");
        epic.setPriority(Priority.NORMAL);
        epic.setComplexity(Complexity.NORMAL);
        epic.setState(State.DRAFT);
        epic.setCost(0.0);
        epic.setEstimatedHours(null);
        epic.setStoryPoints(null);
        epic.setSource("Source");
        epic.setScope(Scope.PROJECT);
        epic.setRisk(Risk.HIGH);
        epic.setCreated(new Date());
        epic.setLastUpdated(new Date());
        epic.setVersion("001");
        epic.setValidationMethod("ValidationMethod");
        epic.setJustification("Justification");
        epic.setTestCases("TestCases");
        epic.setAuthor(user);
        epic.setAssignedTo(user);
        epic.setObject(objectEntity);
        epic.setUserStories(userStoryList);

        objectEntity.setIdobject(epic.getIdEpic());
        objectEntity.setProject(project);

        when(project.getIdProject()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);
        when(userStoryConverter.toDomain(userStoryList)).thenReturn(userStoryDomList);

        EpicConverter epicConverter = createEpicConverter();

        EpicDom obtained = epicConverter.toDomain(epic);

        assertNotNull(obtained);
        assertEquals(obtained.getProjectId(), 1);
        assertEquals(obtained.getIdEpic(), epic.getIdEpic());
        assertEquals(obtained.getName(), epic.getName());
        assertEquals(obtained.getIdentifier(), epic.getIdentifier());
        assertEquals(obtained.getDescription(), epic.getDescription());
        assertEquals(obtained.getDefinitionOfDone(), epic.getDefinitionOfDone());
        assertEquals(obtained.getPriority(), epic.getPriority());
        assertEquals(obtained.getComplexity(), epic.getComplexity());
        assertEquals(obtained.getState(), epic.getState());
        assertEquals(obtained.getSource(), epic.getSource());
        assertEquals(obtained.getScope(), epic.getScope());
        assertEquals(obtained.getRisk(), epic.getRisk());
        assertEquals(obtained.getCreated(), epic.getCreated());
        assertEquals(obtained.getLastUpdated(), epic.getLastUpdated());
        assertEquals(obtained.getVersion(), epic.getVersion());
        assertEquals(obtained.getJustification(), epic.getJustification());
        assertEquals(obtained.getTestCases(), epic.getTestCases());
        //Verify userStoryConverter has been called
        verify(userStoryConverter, times(1)).toDomain(userStoryList);
        verify(userConverter,times(2)).toDomain(user);
    }

    @Test
    @Description("Method toDomain receives an EpicDom and returns an Epic")
    public void toModel_EpicDomProvided_EpicReturned() {
        EpicDom epicDom = new EpicDom();
        epicDom.setIdEpic(1);
        epicDom.setName("EpicTest");
        epicDom.setIdentifier("Epic-1");
        epicDom.setDescription("Description");
        epicDom.setDefinitionOfDone("DoD");
        epicDom.setPriority(Priority.NORMAL);
        epicDom.setComplexity(Complexity.NORMAL);
        epicDom.setState(State.DRAFT);
        epicDom.setCost(0.0);
        epicDom.setEstimatedHours(0.0);
        epicDom.setStoryPoints(0);
        epicDom.setSource("Source");
        epicDom.setScope(Scope.PROJECT);
        epicDom.setRisk(Risk.HIGH);
        epicDom.setCreated(new Date());
        epicDom.setLastUpdated(new Date());
        epicDom.setVersion("001");
        epicDom.setValidationMethod("ValidationMethod");
        epicDom.setJustification("Justification");
        epicDom.setTestCases("TestCases");

        EpicConverter epicConverter = createEpicConverter();

        Epic obtained = epicConverter.toModel(epicDom);

        //asserts
        assertNotNull(obtained);
        assertEquals(obtained.getObject().getIdobject(), 1);
        assertNotNull(obtained.getObject().getTraced());
        assertTrue(obtained.getObject().getTraced().isEmpty());
        assertNull(obtained.getObject().getProject());
        assertEquals(obtained.getIdEpic(), epicDom.getIdEpic());
        assertEquals(obtained.getName(), epicDom.getName());
        assertEquals(obtained.getIdentifier(), epicDom.getIdentifier());
        assertEquals(obtained.getDescription(), epicDom.getDescription());
        assertEquals(obtained.getDefinitionOfDone(), epicDom.getDefinitionOfDone());
        assertEquals(obtained.getPriority(), epicDom.getPriority());
        assertEquals(obtained.getComplexity(), epicDom.getComplexity());
        assertEquals(obtained.getState(), epicDom.getState());
        assertEquals(obtained.getSource(), epicDom.getSource());
        assertEquals(obtained.getScope(), epicDom.getScope());
        assertEquals(obtained.getRisk(), epicDom.getRisk());
        assertEquals(obtained.getCreated(), epicDom.getCreated());
        assertEquals(obtained.getLastUpdated(), epicDom.getLastUpdated());
        assertEquals(obtained.getVersion(), epicDom.getVersion());
        assertEquals(obtained.getJustification(), epicDom.getJustification());
        assertEquals(obtained.getTestCases(), epicDom.getTestCases());
    }

    private EpicConverter createEpicConverter(){
        return new EpicConverter(userStoryConverter, userConverter);
    }
}

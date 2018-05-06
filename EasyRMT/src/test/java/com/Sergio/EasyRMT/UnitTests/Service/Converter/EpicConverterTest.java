/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.UserStory;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicConverterTest {

    /*@Mock
    UserStoryConverter userStoryConverter;

    @BeforeEach
    public void initMocks(){
        userStoryConverter = mock(UserStoryConverter.class);
    }

    @Test
    @Description("Method toDomain receives a list of Epic and returns a list of EpicDom")
    public void toDomain_EpicListProvided_EpicDomListReturned(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        List<UserStory> userStoryList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);
        Epic epic = new Epic();
        epic.setIdEpic(1);
        epic.setName("EpicTest");
        epic.setIdentifier("1234");
        epic.setAuthor(0);
        epic.setAssignedTo(null);
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

    }



    private EpicConverter createEpicConverter(){
        return new EpicConverter(userStoryConverter);
    }*/
}

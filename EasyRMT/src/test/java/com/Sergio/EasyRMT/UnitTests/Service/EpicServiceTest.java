/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.EpicService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicServiceTest {

    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private EpicRepository epicRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EpicConverter epicConverter;

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        epicRepository = mock(EpicRepository.class);
        projectRepository = mock(ProjectRepository.class);
        epicConverter = mock(EpicConverter.class);
    }

    @Test
    @DisplayName("Get a list of epics")
    public void getListEpic_ProjectIdProvided_ListReturned(){
        List<Epic> epicList = mock(List.class);
        List<EpicDom> epicDomList = mock(List.class);

        when(epicRepository.findByProjectID(anyInt())).thenReturn(epicList);
        when(epicConverter.toDomain(epicList)).thenReturn(epicDomList);

        EpicService epicService = createEpicService();
        //Test conditions
        assertTrue(epicService.getEpics(7).equals(epicDomList));
        //Verify epicRepository has been called one time
        verify(epicRepository,times(1)).findByProjectID(7);
        //Verify epicConverter has been called one time
        verify(epicConverter,times(1)).toDomain(epicList);
    }

    @Test
    @DisplayName("Get an epic")
    public void getEpic_EpicIdProvided_EpicReturned(){
        Epic epic = mock(Epic.class);
        EpicDom epicDom = mock(EpicDom.class);

        when(epicRepository.findOne(anyInt())).thenReturn(epic);
        when(epicConverter.toDomain(epic)).thenReturn(epicDom);

        EpicService epicService = createEpicService();
        //Test conditions
        assertTrue(epicService.getEpic(7).equals(epicDom));
        //Verify epicRepository has been called one time
        verify(epicRepository,times(1)).findOne(7);
        //Verify epicConverter has been called one time
        verify(epicConverter,times(1)).toDomain(epic);
    }

    private EpicService createEpicService(){
        return new EpicService(objectRepository,epicRepository,projectRepository,epicConverter);
    }

}

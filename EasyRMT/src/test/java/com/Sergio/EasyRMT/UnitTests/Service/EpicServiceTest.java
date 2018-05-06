/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Model.Epic;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.Project;
import com.Sergio.EasyRMT.Model.UserStory;
import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.EpicService;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EpicServiceTest {

 /*   @Mock
    private ObjectRepository objectRepository;
    @Mock
    private EpicRepository epicRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private EpicConverter epicConverter;
    @Mock
    private DocumentService documentService;

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        epicRepository = mock(EpicRepository.class);
        projectRepository = mock(ProjectRepository.class);
        epicConverter = mock(EpicConverter.class);
        documentService = mock(DocumentService.class);
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

    @Test
    @DisplayName("Create epic persists an epic from an epic provided")
    public void createEpic_EpicProvided_ReturnsEpicPersisted(){
        EpicDom epicDom = mock(EpicDom.class);
        Epic epic = mock(Epic.class);
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);

        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(epicConverter.toModel(epicDom)).thenReturn(epic);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicConverter.toDomain(epic)).thenReturn(epicDom);


        EpicService epicService = createEpicService();

        //Test conditions
        assertTrue(epicService.create(epicDom,7).equals(epicDom));
        //Verify that epicConverter.toModel has been called once
        verify(epicConverter, times(1)).toModel(epicDom);
        //verify that projectRepository.save has been called once
        verify(projectRepository, times(1)).findByIdProject(7);
        //verify that epicConverter.toDomain has been called once
        verify(epicConverter, times(1)).toDomain(epic);
        //verify objectRepository has been called
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        //verify epic repository has been called
        verify(epicRepository, times(1)).save(epic);
        //verify epic.setIdeEpic has been called
        verify(epic, times(1)).setIdEpic(9);
        //verify object entity. get id object has been called
        verify(objectEntity,times(1)).getIdobject();
        //verify epic.setAuthor has been called
        verify(epicDom, times(1)).setAuthor(anyInt());
        //verify setUserStories has been called
        verify(epic, times(1)).setUserStories(anyList());
        //Verify epic created has been called
        verify(epicDom, times(1)).setCreated(any(Date.class));
        //Verify epic last updated has been called
        verify(epicDom, times(1)).setLastUpdated(any(Date.class));
    }

    @Test
    @DisplayName("DeleteEpic deletes an epic and all its children")
    public void deleteEpic_IdObjectProvided_DeletesEpicObjectAndChildren(){
        Epic epic = mock(Epic.class);
        List<UserStory> userStories = new ArrayList<>();
        UserStory us1 = new UserStory();
        us1.setIdUserStory(1);
        userStories.add(us1);
        UserStory userStory = mock(UserStory.class);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(epic.getUserStories()).thenReturn(userStories);
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(epicRepository).exists(anyInt());
        when(epicRepository.findOne(anyInt())).thenReturn(epic);

        EpicService epicService = createEpicService();

        assertTrue(epicService.deleteEpic(7));
        //Verify that method calls two times to project repository.existsByIdProject
        verify(objectRepository, times(2)).exists(anyInt());
        //Verify that projectRepository.deleteProjectByIdProject() is called once
        verify(objectRepository, times(2)).deleteObject(anyInt());
        //verify epic repository has been called
        verify(epicRepository, times(1)).findOne(anyInt());
        verify(epicRepository,times(1)).exists(anyInt());
        //verify epic has been called
        verify(epic, times(2)).getUserStories();
    }

    @Test
    @DisplayName("Delete Epic fails when epic does not exists in DB")
    public void deleteEpic_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        EpicService epicService = createEpicService();

        //Test conditions
        assertFalse(epicService.deleteEpic(7));
        //Verify that method calls one time to object repository.exists
        verify(objectRepository, times(1)).exists(anyInt());
        //Verify that epicRepository.findOne() is never called
        verify(epicRepository, times(0)).findOne(anyInt());
        //Verify that objectRepository.deleteObject() is never called
        verify(objectRepository, times(0)).deleteObject(anyInt());
    }

    @Test
    @DisplayName("Update epic persist infomation")
    public void updateEpic_EpicModified_EpicUpdated(){


    }

    private EpicService createEpicService(){
        return new EpicService(objectRepository,epicRepository,projectRepository,epicConverter, documentService);
    }
*/
}

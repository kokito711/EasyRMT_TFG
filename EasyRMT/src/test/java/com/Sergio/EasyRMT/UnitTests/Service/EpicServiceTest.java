/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.EpicRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.ProjectRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
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

import static org.junit.Assert.*;
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
    @Mock
    private DocumentService documentService;
    @Mock
    private UserRepository userRepository;

    private Date date;

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        epicRepository = mock(EpicRepository.class);
        projectRepository = mock(ProjectRepository.class);
        epicConverter = mock(EpicConverter.class);
        documentService = mock(DocumentService.class);
        userRepository = mock(UserRepository.class);
        date = new Date();
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
        Epic epic = createEpic();
        EpicDom epicDom = createEpicDom();

        when(epicRepository.findOne(1)).thenReturn(epic);
        when(epicConverter.toDomain(epic)).thenReturn(epicDom);

        EpicService epicService = createEpicService();
        EpicDom obtained = epicService.getEpic(1);
        //Test conditions
        assertEquals(obtained, epicDom);
        assertNotNull(obtained);
        //Verify epicRepository has been called one time
        verify(epicRepository,times(1)).findOne(1);
        //Verify epicConverter has been called one time
        verify(epicConverter,times(1)).toDomain(epic);
    }

    @Test
    @DisplayName("Create epic persists an epic from an epic provided")
    public void createEpic_EpicProvided_ReturnsEpicPersisted(){
        EpicDom epicDom = createEpicDom();
        Epic epic = createEpic();
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        User user = epic.getAssignedTo();


        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(epicConverter.toModel(epicDom)).thenReturn(epic);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicConverter.toDomain(epic)).thenReturn(epicDom);
        when(userRepository.findOne(epicDom.getAuthorId())).thenReturn(user);
        when(userRepository.findOne(epicDom.getAssignedId())).thenReturn(user);


        EpicService epicService = createEpicService();

        EpicDom obtained = epicService.create(epicDom,7);

        //Test conditions
        assertEquals(obtained,epicDom);
        assertNotNull(obtained);

        verify(epicConverter, times(1)).toModel(epicDom);
        verify(projectRepository, times(1)).findByIdProject(7);
        verify(epicConverter, times(1)).toDomain(epic);
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        verify(epicRepository, times(1)).save(epic);
        verify(objectEntity,times(1)).getIdobject();
        verify(userRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("DeleteEpic deletes an epic and all its children")
    public void deleteEpic_IdObjectProvided_DeletesEpicObjectAndChildren(){
        Epic epic = createEpic();
        List<UserStory> userStories = new ArrayList<>();
        UserStory userStory = mock(UserStory.class);
        userStories.add(userStory);
        epic.setUserStories(userStories);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(userStory.getObject()).thenReturn(objectEntity);
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(objectRepository).deleteObject(anyInt());
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doReturn(false).when(epicRepository).exists(anyInt());
        when(epicRepository.findOne(anyInt())).thenReturn(epic);

        EpicService epicService = createEpicService();

        assertTrue(epicService.deleteEpic(7));
        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(2)).deleteObject(anyInt());
        verify(epicRepository,times(1)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("deleteEpic fails to delete an epic")
    public void deleteEpic_IdObjectProvided_DeletionFailed(){
        Epic epic = mock(Epic.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(true).when(objectRepository).exists(anyInt());
        when(epic.getObject()).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(epicRepository).exists(anyInt());
        when(epicRepository.findOne(anyInt())).thenReturn(epic);

        EpicService epicService = createEpicService();

        assertFalse(epicService.deleteEpic(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(epicRepository,times(0)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("deleteEpic fails when epic does not exists in DB")
    public void deleteEpic_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        EpicService epicService = createEpicService();

        //Test conditions
        assertFalse(epicService.deleteEpic(7));

        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).deleteObject(anyInt());
        verify(epicRepository,times(0)).exists(anyInt());
        verify(documentService, times(0)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Update Requirement persist infomation")
    public void updateRequirement_RequirementModified_RequirementUpdated(){
        EpicDom epicDom = new EpicDom();
        UserDom userDom2 = mock(UserDom.class);
        epicDom.setIdEpic(1);
        epicDom.setName("Test1");
        epicDom.setIdentifier("12345");
        epicDom.setDescription("description1");
        epicDom.setPriority(Priority.BLOCKER);
        epicDom.setComplexity(Complexity.HIGH);
        epicDom.setState(State.APPROVED);
        epicDom.setCost(27.0);
        epicDom.setEstimatedHours(29.00);
        epicDom.setStoryPoints(5);
        epicDom.setSource("source1");
        epicDom.setScope(Scope.PROJECT);
        epicDom.setRisk(Risk.LOW);
        epicDom.setCreated(date);
        epicDom.setLastUpdated(date);
        epicDom.setVersion("version1");
        epicDom.setValidationMethod("validation1");
        epicDom.setAuthor(userDom2);
        epicDom.setAssignedTo(userDom2);
        epicDom.setJustification("justification1");
        epicDom.setTestCases("test cases1");
        epicDom.setDefinitionOfDone("DuD");
        epicDom.setProjectId(1);
        epicDom.setAssignedId(1);

        Epic epic = createEpic();
        User user = epic.getAssignedTo();
        when(epicRepository.findOne(1)).thenReturn(epic);
        when(userRepository.findOne(epicDom.getAssignedId())).thenReturn(user);
        when(user.getUserId()).thenReturn(5);
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicConverter.toDomain(any(Epic.class))).thenReturn(epicDom);

        EpicService epicService = createEpicService();
        EpicDom obtained = epicService.update(epicDom,1,1);

        assertEquals(obtained,epicDom);
        assertNotNull(obtained);
        verify(epicRepository,times(1)).findOne(anyInt());
        verify(epicRepository,times(1)).save(any(Epic.class));
        verify(epicConverter, times(1)).toDomain(any(Epic.class));
        verify(userRepository, times(1)).findOne(anyInt());
    }

    private EpicService createEpicService(){
        return new EpicService(objectRepository,epicRepository,projectRepository,epicConverter, documentService,
                userRepository);
    }

    private EpicDom createEpicDom(){
        EpicDom epicDom = new EpicDom();
        epicDom.setIdEpic(1);
        epicDom.setName("Test");
        epicDom.setIdentifier("1234");
        epicDom.setDescription("description");
        epicDom.setPriority(Priority.NORMAL);
        epicDom.setComplexity(Complexity.NORMAL);
        epicDom.setState(State.DRAFT);
        epicDom.setCost(0.0);
        epicDom.setEstimatedHours(27.00);
        epicDom.setStoryPoints(0);
        epicDom.setSource("source");
        epicDom.setScope(Scope.FEATURE);
        epicDom.setRisk(Risk.HIGH);
        epicDom.setCreated(date);
        epicDom.setLastUpdated(date);
        epicDom.setVersion("version");
        epicDom.setValidationMethod("validation");
        epicDom.setJustification("justification");
        epicDom.setTestCases("test cases");
        epicDom.setDefinitionOfDone("DoD");
        epicDom.setProjectId(1);

        UserDom userDom = mock(UserDom.class);
        epicDom.setAssignedTo(userDom);
        epicDom.setAuthor(userDom);
        return epicDom;
    }

    private  Epic createEpic(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        Epic epic = new Epic();
        epic.setIdEpic(1);
        epic.setName("Test");
        epic.setIdentifier("1234");
        epic.setDescription("description");
        epic.setPriority(Priority.NORMAL);
        epic.setComplexity(Complexity.NORMAL);
        epic.setState(State.DRAFT);
        epic.setCost(0.0);
        epic.setEstimatedHours(27.00);
        epic.setStoryPoints(0);
        epic.setSource("source");
        epic.setScope(Scope.FEATURE);
        epic.setRisk(Risk.HIGH);
        epic.setCreated(date);
        epic.setLastUpdated(date);
        epic.setVersion("version");
        epic.setValidationMethod("validation");
        epic.setJustification("justification");
        epic.setTestCases("test cases");
        epic.setDefinitionOfDone("DoD");
        epic.setObject(objectEntity);

        User user = mock(User.class);
        epic.setAssignedTo(user);
        epic.setAuthor(user);
        return epic;
    }

}

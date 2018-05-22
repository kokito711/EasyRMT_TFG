package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.UserStoryService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserStoryServiceTest {
    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private EpicRepository epicRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private UserStoryConverter userStoryConverter;
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
        userStoryRepository = mock(UserStoryRepository.class);
        userStoryConverter = mock(UserStoryConverter.class);
        documentService = mock(DocumentService.class);
        userRepository = mock(UserRepository.class);
        date = new Date();
    }

    @Test
    @DisplayName("Get a list of user stories")
    public void getListUserStory_EpicIdProvided_ListReturned(){
        List<UserStory> userStoryList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);

        when(userStoryRepository.findByEpicId(anyInt())).thenReturn(userStoryList);
        when(userStoryConverter.toDomain(userStoryList)).thenReturn(userStoryDomList);

        UserStoryService userStoryService = createUserStoryService();
        //Test conditions
        assertTrue(userStoryService.getUserStories(7).equals(userStoryDomList));
        //Verify epicRepository has been called one time
        verify(userStoryRepository,times(1)).findByEpicId(7);
        //Verify epicConverter has been called one time
        verify(userStoryConverter,times(1)).toDomain(userStoryList);
    }

    @Test
    @DisplayName("Get a list of user stories")
    public void getListUserStory_ProjectIdProvided_ListReturned(){
        List<UserStory> userStoryList = mock(List.class);
        List<UserStoryDom> userStoryDomList = mock(List.class);

        when(userStoryRepository.findByProjectId(anyInt())).thenReturn(userStoryList);
        when(userStoryConverter.toDomain(userStoryList)).thenReturn(userStoryDomList);

        UserStoryService userStoryService = createUserStoryService();
        //Test conditions
        assertTrue(userStoryService.getByProjectID(7).equals(userStoryDomList));
        //Verify epicRepository has been called one time
        verify(userStoryRepository,times(1)).findByProjectId(7);
        //Verify epicConverter has been called one time
        verify(userStoryConverter,times(1)).toDomain(userStoryList);
    }

    @Test
    @DisplayName("Get a user Story")
    public void getUserStory_UserStoryIdProvided_UserStoryReturned(){
        UserStory userStory = createUserStory();
        UserStoryDom userStoryDom = createUserStoryDom();

        when(userStoryRepository.findOne(1)).thenReturn(userStory);
        when(userStoryConverter.toDomain(userStory)).thenReturn(userStoryDom);

        UserStoryService userStoryService = createUserStoryService();
        UserStoryDom obtained = userStoryService.getUserStory(1);
        //Test conditions
        assertEquals(obtained, userStoryDom);
        assertNotNull(obtained);
        //Verify epicRepository has been called one time
        verify(userStoryRepository,times(1)).findOne(1);
        //Verify epicConverter has been called one time
        verify(userStoryConverter,times(1)).toDomain(userStory);
    }

    @Test
    @DisplayName("Create userStory persists an epic from a user story provided")
    public void createUserStory_UserStoryProvided_ReturnsUserStoryPersisted(){
        UserStoryDom userStoryDom = createUserStoryDom();
        UserStory userStory = createUserStory();
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        User user = userStory.getAssignedTo();
        Epic epic = mock(Epic.class);
        List<ObjectEntity> traceability = mock(List.class);

        when(objectEntity.getTraced()).thenReturn(traceability);
        doReturn(true).doReturn(true).when(traceability).add(objectEntity);
        when(epicRepository.findOne(1)).thenReturn(epic);
        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(userStoryConverter.toModel(userStoryDom)).thenReturn(userStory);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(userStoryRepository.save(userStory)).thenReturn(userStory);
        when(userStoryConverter.toDomain(userStory)).thenReturn(userStoryDom);
        when(userRepository.findOne(userStoryDom.getAuthorId())).thenReturn(user);
        when(userRepository.findOne(userStoryDom.getAssignedId())).thenReturn(user);
        when(objectRepository.findOne(anyInt())).thenReturn(objectEntity);
        when(objectRepository.save(objectEntity)).thenReturn(objectEntity);

        UserStoryService userStoryService = createUserStoryService();

        UserStoryDom obtained = userStoryService.create(userStoryDom,1,7);

        //Test conditions
        assertEquals(obtained,userStoryDom);
        assertNotNull(obtained);
        verify(userStoryConverter, times(1)).toModel(userStoryDom);
        verify(projectRepository, times(1)).findByIdProject(7);
        verify(userStoryConverter, times(1)).toDomain(userStory);
        verify(objectRepository, times(3)).save(any(ObjectEntity.class));
        verify(epicRepository, times(1)).findOne(1);
        verify(objectEntity,times(1)).getIdobject();
        verify(userRepository, times(2)).findOne(anyInt());
        verify(userStoryRepository, times(1)).save(userStory);
        verify(objectRepository, times(1)).findOne(1);
    }

    @Test
    @DisplayName("Delete userStory deletes a userStory")
    public void deleteUserStory_IdObjectProvided_DeletesUserStory(){
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(userStoryRepository).exists(anyInt());

        UserStoryService userStoryService = createUserStoryService();

        assertTrue(userStoryService.deleteUserStory(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(userStoryRepository,times(1)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Delete userStory fails to delete a userStory")
    public void deleteUserStory_IdObjectProvided_DeletionFailed(){
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(true).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(userStoryRepository).exists(anyInt());

        UserStoryService userStoryService = createUserStoryService();

        assertFalse(userStoryService.deleteUserStory(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(userStoryRepository,times(0)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Delete userStory fails when userStory does not exists in DB")
    public void deleteRequirement_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        UserStoryService userStoryService = createUserStoryService();

        //Test conditions
        assertFalse(userStoryService.deleteUserStory(7));

        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).deleteObject(anyInt());
        verify(userStoryRepository,times(0)).exists(anyInt());
        verify(documentService, times(0)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Update Requirement persist infomation")
    public void updateRequirement_RequirementModified_RequirementUpdated(){
        UserStoryDom userStoryDom = new UserStoryDom();
        UserDom userDom2 = mock(UserDom.class);
        userStoryDom.setIdUserStory(1);
        userStoryDom.setName("Test1");
        userStoryDom.setIdentifier("12345");
        userStoryDom.setDescription("description1");
        userStoryDom.setPriority(Priority.BLOCKER);
        userStoryDom.setComplexity(Complexity.HIGH);
        userStoryDom.setState(State.APPROVED);
        userStoryDom.setCost(27.0);
        userStoryDom.setEstimatedHours(29.00);
        userStoryDom.setStoryPoints(5);
        userStoryDom.setSource("source1");
        userStoryDom.setScope(Scope.PROJECT);
        userStoryDom.setRisk(Risk.LOW);
        userStoryDom.setCreated(date);
        userStoryDom.setLastUpdated(date);
        userStoryDom.setVersion("version1");
        userStoryDom.setValidationMethod("validation1");
        userStoryDom.setAuthor(userDom2);
        userStoryDom.setAssignedTo(userDom2);
        userStoryDom.setJustification("justification1");
        userStoryDom.setTestCases("test cases1");
        userStoryDom.setDefinitionOfDone("DuD");
        userStoryDom.setProjectId(1);
        userStoryDom.setAssignedId(1);
        userStoryDom.setEpicId(1);

        UserStory userStory = createUserStory();
        User user = userStory.getAssignedTo();
        when(userStoryRepository.findOne(1)).thenReturn(userStory);
        when(userRepository.findOne(userStoryDom.getAssignedId())).thenReturn(user);
        when(user.getUserId()).thenReturn(5);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);
        when(userStoryConverter.toDomain(any(UserStory.class))).thenReturn(userStoryDom);

        UserStoryService userStoryService = createUserStoryService();
        UserStoryDom obtained = userStoryService.update(1,1,1, userStoryDom);

        assertEquals(obtained,userStoryDom);
        assertNotNull(obtained);
        verify(userStoryRepository,times(1)).findOne(anyInt());
        verify(userStoryRepository,times(1)).save(any(UserStory.class));
        verify(userStoryConverter, times(1)).toDomain(any(UserStory.class));
        verify(userRepository, times(1)).findOne(anyInt());
    }

    private UserStoryService createUserStoryService(){
        return new UserStoryService(
                objectRepository, epicRepository, projectRepository, userStoryRepository,
                userStoryConverter,  documentService, userRepository
        );
    }

    private UserStoryDom createUserStoryDom(){
        UserStoryDom userStoryDom = new UserStoryDom();
        userStoryDom.setIdUserStory(1);
        userStoryDom.setName("Test");
        userStoryDom.setIdentifier("1234");
        userStoryDom.setDescription("description");
        userStoryDom.setPriority(Priority.NORMAL);
        userStoryDom.setComplexity(Complexity.NORMAL);
        userStoryDom.setState(State.DRAFT);
        userStoryDom.setCost(0.0);
        userStoryDom.setEstimatedHours(27.00);
        userStoryDom.setStoryPoints(0);
        userStoryDom.setSource("source");
        userStoryDom.setScope(Scope.FEATURE);
        userStoryDom.setRisk(Risk.HIGH);
        userStoryDom.setCreated(date);
        userStoryDom.setLastUpdated(date);
        userStoryDom.setVersion("version");
        userStoryDom.setValidationMethod("validation");
        userStoryDom.setJustification("justification");
        userStoryDom.setTestCases("test cases");
        userStoryDom.setDefinitionOfDone("DoD");
        userStoryDom.setProjectId(1);

        UserDom userDom = mock(UserDom.class);
        userStoryDom.setAssignedTo(userDom);
        userStoryDom.setAuthor(userDom);
        return userStoryDom;
    }

    private  UserStory createUserStory(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        UserStory userStory = new UserStory();
        userStory.setIdUserStory(1);
        userStory.setName("Test");
        userStory.setIdentifier("1234");
        userStory.setDescription("description");
        userStory.setPriority(Priority.NORMAL);
        userStory.setComplexity(Complexity.NORMAL);
        userStory.setState(State.DRAFT);
        userStory.setCost(0.0);
        userStory.setEstimatedHours(27.00);
        userStory.setStoryPoints(0);
        userStory.setSource("source");
        userStory.setScope(Scope.FEATURE);
        userStory.setRisk(Risk.HIGH);
        userStory.setCreated(date);
        userStory.setLastUpdated(date);
        userStory.setVersion("version");
        userStory.setValidationMethod("validation");
        userStory.setJustification("justification");
        userStory.setTestCases("test cases");
        userStory.setDefinitionOfDone("DoD");
        userStory.setObject(objectEntity);

        User user = mock(User.class);
        userStory.setAssignedTo(user);
        userStory.setAuthor(user);
        return userStory;
    }
}

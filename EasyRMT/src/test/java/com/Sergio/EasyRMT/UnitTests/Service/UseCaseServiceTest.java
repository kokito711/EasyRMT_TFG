package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.UseCaseConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.UseCaseService;
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
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class UseCaseServiceTest {
    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private FeatureRepository featureRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UseCaseRepository useCaseRepository;
    @Mock
    private UseCaseConverter useCaseConverter;
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
        useCaseRepository = mock(UseCaseRepository.class);
        useCaseConverter = mock(UseCaseConverter.class);
        documentService = mock(DocumentService.class);
        userRepository = mock(UserRepository.class);
        date = new Date();
    }

    @Test
    @DisplayName("Get a list of use cases")
    public void getListUseCase_FeatureIdProvided_ListReturned(){
        List<UseCase> useCaseList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);

        when(useCaseRepository.findByFeatureId(anyInt())).thenReturn(useCaseList);
        when(useCaseConverter.toDomain(useCaseList)).thenReturn(useCaseDomList);

        UseCaseService useCaseService = createUseCaseService();
        //Test conditions
        assertTrue(useCaseService.getUseCases(7).equals(useCaseDomList));
        //Verify epicRepository has been called one time
        verify(useCaseRepository,times(1)).findByFeatureId(7);
        //Verify epicConverter has been called one time
        verify(useCaseConverter,times(1)).toDomain(useCaseList);
    }

    @Test
    @DisplayName("Get a list of Use Cases")
    public void getListUseCase_ProjectIdProvided_ListReturned(){
        List<UseCase> useCaseList = mock(List.class);
        List<UseCaseDom> useCaseDomList = mock(List.class);

        when(useCaseRepository.findByProjectId(anyInt())).thenReturn(useCaseList);
        when(useCaseConverter.toDomain(useCaseList)).thenReturn(useCaseDomList);

        UseCaseService useCaseService = createUseCaseService();
        //Test conditions
        assertTrue(useCaseService.getByProjectID(7).equals(useCaseDomList));
        //Verify epicRepository has been called one time
        verify(useCaseRepository,times(1)).findByProjectId(7);
        //Verify epicConverter has been called one time
        verify(useCaseConverter,times(1)).toDomain(useCaseList);
    }

    @Test
    @DisplayName("Get a Use Case")
    public void getUseCase_UseCaseIdProvided_UseCaseReturned(){
        UseCase useCase = createUseCase();
        UseCaseDom useCaseDom = createUseCaseDom();

        when(useCaseRepository.findOne(1)).thenReturn(useCase);
        when(useCaseConverter.toDomain(useCase)).thenReturn(useCaseDom);

        UseCaseService useCaseService = createUseCaseService();
        UseCaseDom obtained = useCaseService.getUseCase(1);
        //Test conditions
        assertEquals(obtained, useCaseDom);
        assertNotNull(obtained);
        //Verify epicRepository has been called one time
        verify(useCaseRepository,times(1)).findOne(1);
        //Verify epicConverter has been called one time
        verify(useCaseConverter,times(1)).toDomain(useCase);
    }

    @Test
    @DisplayName("Create UseCase persists a UseCase from a UseCaseDom provided")
    public void createUseCase_UseCaseProvided_ReturnsUseCasePersisted(){
        UseCaseDom useCaseDom = createUseCaseDom();
        UseCase useCase = createUseCase();
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        User user = useCase.getAssignedTo();
        Feature feature = mock(Feature.class);
        List<ObjectEntity> traceability = mock(List.class);

        when(objectEntity.getTraced()).thenReturn(traceability);
        doReturn(true).doReturn(true).when(traceability).add(objectEntity);
        when(featureRepository.findOne(1)).thenReturn(feature);
        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(useCaseConverter.toModel(useCaseDom)).thenReturn(useCase);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(useCaseRepository.save(useCase)).thenReturn(useCase);
        when(useCaseConverter.toDomain(useCase)).thenReturn(useCaseDom);
        when(userRepository.findOne(useCaseDom.getAuthorId())).thenReturn(user);
        when(userRepository.findOne(useCaseDom.getAssignedId())).thenReturn(user);
        when(objectRepository.findOne(anyInt())).thenReturn(objectEntity);
        when(objectRepository.save(objectEntity)).thenReturn(objectEntity);

        UseCaseService useCaseService = createUseCaseService();

        UseCaseDom obtained = useCaseService.create(useCaseDom,1,7);

        //Test conditions
        assertEquals(obtained,useCaseDom);
        assertNotNull(obtained);
        verify(useCaseConverter, times(1)).toModel(useCaseDom);
        verify(projectRepository, times(1)).findByIdProject(7);
        verify(useCaseConverter, times(1)).toDomain(useCase);
        verify(objectRepository, times(3)).save(any(ObjectEntity.class));
        verify(featureRepository, times(1)).findOne(1);
        verify(objectEntity,times(1)).getIdobject();
        verify(userRepository, times(2)).findOne(anyInt());
        verify(useCaseRepository, times(1)).save(useCase);
        verify(objectRepository, times(1)).findOne(1);
    }

    @Test
    @DisplayName("Delete UseCase deletes a UseCase")
    public void deleteUseCase_IdObjectProvided_DeletesUseCase(){
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(useCaseRepository).exists(anyInt());

        UseCaseService useCaseService = createUseCaseService();

        assertTrue(useCaseService.deleteUseCase(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(useCaseRepository,times(1)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Delete UseCase fails to delete a UseCase")
    public void deleteUseCase_IdObjectProvided_DeletionFailed(){
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(true).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(useCaseRepository).exists(anyInt());

        UseCaseService useCaseService = createUseCaseService();

        assertFalse(useCaseService.deleteUseCase(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(useCaseRepository,times(0)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Delete UseCase fails when UseCase does not exists in DB")
    public void deleteUseCase_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        UseCaseService useCaseService = createUseCaseService();

        //Test conditions
        assertFalse(useCaseService.deleteUseCase(7));

        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).deleteObject(anyInt());
        verify(useCaseRepository,times(0)).exists(anyInt());
        verify(documentService, times(0)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Update UseCase persist infomation")
    public void updateUseCase_UseCaseModified_UseCaseUpdated(){
        UseCaseDom useCaseDom = new UseCaseDom();
        UserDom userDom2 = mock(UserDom.class);
        useCaseDom.setIdUseCase(1);
        useCaseDom.setName("Test1");
        useCaseDom.setIdentifier("12345");
        useCaseDom.setDescription("description1");
        useCaseDom.setPriority(Priority.BLOCKER);
        useCaseDom.setComplexity(Complexity.HIGH);
        useCaseDom.setState(State.APPROVED);
        useCaseDom.setCost(27.0);
        useCaseDom.setEstimatedHours(29.00);
        useCaseDom.setStoryPoints(5);
        useCaseDom.setSource("source1");
        useCaseDom.setScope(Scope.PROJECT);
        useCaseDom.setRisk(Risk.LOW);
        useCaseDom.setCreated(date);
        useCaseDom.setLastUpdated(date);
        useCaseDom.setVersion("version1");
        useCaseDom.setValidationMethod("validation1");
        useCaseDom.setAuthor(userDom2);
        useCaseDom.setAssignedTo(userDom2);
        useCaseDom.setJustification("justification1");
        useCaseDom.setTestCases("test cases1");
        useCaseDom.setActors("Actors1");
        useCaseDom.setPreconditions("Preconditions1");
        useCaseDom.setPostconditions("PostConditions1");
        useCaseDom.setSteps("Steps1");
        useCaseDom.setProjectId(1);
        useCaseDom.setAssignedId(1);
        useCaseDom.setFeatureId(1);

        UseCase useCase = createUseCase();
        User user = useCase.getAssignedTo();
        when(useCaseRepository.findOne(1)).thenReturn(useCase);
        when(userRepository.findOne(useCaseDom.getAssignedId())).thenReturn(user);
        when(user.getUserId()).thenReturn(5);
        when(useCaseRepository.save(any(UseCase.class))).thenReturn(useCase);
        when(useCaseConverter.toDomain(any(UseCase.class))).thenReturn(useCaseDom);

        UseCaseService useCaseService = createUseCaseService();
        UseCaseDom obtained = useCaseService.update(1,1,1, useCaseDom);

        assertEquals(obtained,useCaseDom);
        assertNotNull(obtained);
        verify(useCaseRepository,times(1)).findOne(anyInt());
        verify(useCaseRepository,times(1)).save(any(UseCase.class));
        verify(useCaseConverter, times(1)).toDomain(any(UseCase.class));
        verify(userRepository, times(1)).findOne(anyInt());
    }

    private UseCaseService createUseCaseService(){
        return new UseCaseService(
          objectRepository,featureRepository, projectRepository, useCaseRepository,
          useCaseConverter, documentService, userRepository
        );
    }

    private UseCaseDom createUseCaseDom(){
        UseCaseDom useCaseDom = new UseCaseDom();
        useCaseDom.setIdUseCase(1);
        useCaseDom.setName("Test");
        useCaseDom.setIdentifier("1234");
        useCaseDom.setDescription("description");
        useCaseDom.setPriority(Priority.NORMAL);
        useCaseDom.setComplexity(Complexity.NORMAL);
        useCaseDom.setState(State.DRAFT);
        useCaseDom.setCost(0.0);
        useCaseDom.setEstimatedHours(27.00);
        useCaseDom.setStoryPoints(0);
        useCaseDom.setSource("source");
        useCaseDom.setScope(Scope.FEATURE);
        useCaseDom.setRisk(Risk.HIGH);
        useCaseDom.setCreated(date);
        useCaseDom.setLastUpdated(date);
        useCaseDom.setVersion("version");
        useCaseDom.setValidationMethod("validation");
        useCaseDom.setJustification("justification");
        useCaseDom.setTestCases("test cases");
        useCaseDom.setActors("Actors");
        useCaseDom.setPreconditions("Preconditions");
        useCaseDom.setPostconditions("PostConditions");
        useCaseDom.setSteps("Steps");
        useCaseDom.setProjectId(1);

        UserDom userDom = mock(UserDom.class);
        useCaseDom.setAssignedTo(userDom);
        useCaseDom.setAuthor(userDom);
        return useCaseDom;
    }

    private UseCase createUseCase(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        UseCase useCase = new UseCase();
        useCase.setIdUseCase(1);
        useCase.setName("Test");
        useCase.setIdentifier("1234");
        useCase.setDescription("description");
        useCase.setPriority(Priority.NORMAL);
        useCase.setComplexity(Complexity.NORMAL);
        useCase.setState(State.DRAFT);
        useCase.setCost(0.0);
        useCase.setEstimatedHours(27.00);
        useCase.setStoryPoints(0);
        useCase.setSource("source");
        useCase.setScope(Scope.FEATURE);
        useCase.setRisk(Risk.HIGH);
        useCase.setCreated(date);
        useCase.setLastUpdated(date);
        useCase.setVersion("version");
        useCase.setValidationMethod("validation");
        useCase.setJustification("justification");
        useCase.setTestCases("test cases");
        useCase.setActors("Actors");
        useCase.setPreconditions("Preconditions");
        useCase.setPostconditions("PostConditions");
        useCase.setSteps("Steps");
        useCase.setObject(objectEntity);

        User user = mock(User.class);
        useCase.setAssignedTo(user);
        useCase.setAuthor(user);
        return useCase;
    }
}

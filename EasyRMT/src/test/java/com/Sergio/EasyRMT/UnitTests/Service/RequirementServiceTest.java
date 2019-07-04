/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.RequirementConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.RequirementService;
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
public class RequirementServiceTest {

    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private RequirementConverter requirementConverter;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RequirementRepository requirementRepository;
    @Mock
    private ReqTypeRepository reqTypeRepository;
    @Mock
    private DocumentService documentService;
    @Mock
    private UserRepository userRepository;

    private Date date;
    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        requirementConverter = mock(RequirementConverter.class);
        projectRepository = mock(ProjectRepository.class);
        requirementRepository = mock(RequirementRepository.class);
        reqTypeRepository = mock(ReqTypeRepository.class);
        documentService = mock(DocumentService.class);
        userRepository = mock(UserRepository.class);
        date = new Date();
    }

    @Test
    @DisplayName("Get a list of requirements")
    public void getListRequirements_ProjectIdProvided_ListReturned(){
        List<Requirement> requirements = mock(List.class);
        List<RequirementDom> requirementDomList = mock(List.class);

        when(requirementRepository.findByProjectID(anyInt())).thenReturn(requirements);
        when(requirementConverter.toDomain(requirements)).thenReturn(requirementDomList);

        RequirementService requirementService = createRequirementService();
        //Test conditions
        assertTrue(requirementService.getRequirements(7).equals(requirementDomList));
        //Verify epicRepository has been called one time
        verify(requirementRepository,times(1)).findByProjectID(7);
        //Verify epicConverter has been called one time
        verify(requirementConverter,times(1)).toDomain(requirements);
    }

    @Test
    @DisplayName("Get a requirement")
    public void getRequirement_RequirementIdProvided_RequirementReturned(){
        Requirement requirement = createRequirement(true);
        RequirementDom expected = createRequirementDom(true);


        when(requirementRepository.findOne(anyInt())).thenReturn(requirement);
        when(requirementConverter.toDomain(requirement)).thenReturn(expected);

        RequirementService requirementService = createRequirementService();
        RequirementDom obtained = requirementService.getRequirement(7);

        //Test conditions
        assertEquals(expected,obtained);
        assertNotNull(expected);
        //Verify epicRepository has been called one time
        verify(requirementRepository,times(1)).findOne(7);
        //Verify epicConverter has been called one time
        verify(requirementConverter,times(1)).toDomain(requirement);
    }

    @Test
    @DisplayName("Create requirement persists an requirement from an requirement provided")
    public void createRequirement_RequirementProvided_ReturnsRequirementPersisted(){
        RequirementDom requirementDom = createRequirementDom(true);
        Requirement requirement = createRequirement(true);
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        RequirementType requirementType = mock(RequirementType.class);
        User user = requirement.getAssignedTo();


        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(requirementConverter.toModel(requirementDom)).thenReturn(requirement);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(requirementRepository.save(requirement)).thenReturn(requirement);
        when(requirementConverter.toDomain(requirement)).thenReturn(requirementDom);
        when(reqTypeRepository.findByIdType(anyInt())).thenReturn(Optional.of(requirementType));
        when(userRepository.findOne(requirementDom.getAuthorId())).thenReturn(user);
        when(userRepository.findOne(requirementDom.getAssignedId())).thenReturn(user);

        RequirementService requirementService = createRequirementService();
        RequirementDom obtained = requirementService.create(requirementDom,7);

        //Test conditions
        assertEquals(obtained,requirementDom);
        assertNotNull(obtained);

        verify(requirementConverter, times(1)).toModel(requirementDom);
        verify(projectRepository, times(1)).findByIdProject(7);
        verify(requirementConverter, times(1)).toDomain(requirement);
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        verify(requirementRepository, times(1)).save(requirement);
        verify(objectEntity,times(1)).getIdobject();
        verify(userRepository, times(2)).findOne(anyInt());
    }

    @Test
    @DisplayName("DeleteRequirement deletes a Requirement")
    public void deleteRequirement_IdObjectProvided_DeletesRequirement(){
        Requirement requirement = mock(Requirement.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(requirementRepository).exists(anyInt());
        when(requirementRepository.findOne(anyInt())).thenReturn(requirement);

        RequirementService requirementService = createRequirementService();

        assertTrue(requirementService.deleteRequirement(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(requirementRepository,times(1)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("DeleteRequirement fails to delete a Requirement")
    public void deleteRequirement_IdObjectProvided_DeletionFailed(){
        Requirement requirement = mock(Requirement.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = new Project();
        project.setIdProject(1);
        objectEntity.setIdobject(7);
        objectEntity.setProject(project);
        doReturn(true).doReturn(true).when(objectRepository).exists(anyInt());
        when(objectRepository.findOne(7)).thenReturn(objectEntity);
        doNothing().when(documentService).deleteFiles(objectEntity.getProject().getIdProject(), objectEntity.getIdobject());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(requirementRepository).exists(anyInt());
        when(requirementRepository.findOne(anyInt())).thenReturn(requirement);

        RequirementService requirementService = createRequirementService();

        assertFalse(requirementService.deleteRequirement(7));

        verify(objectRepository, times(2)).exists(anyInt());
        verify(objectRepository, times(1)).deleteObject(anyInt());
        verify(requirementRepository,times(0)).exists(anyInt());
        verify(documentService, times(1)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Delete Requirement fails when req does not exists in DB")
    public void deleteRequirement_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        RequirementService requirementService = createRequirementService();

        //Test conditions
        assertFalse(requirementService.deleteRequirement(7));

        verify(objectRepository, times(1)).exists(anyInt());
        verify(objectRepository, times(0)).deleteObject(anyInt());
        verify(requirementRepository,times(0)).exists(anyInt());
        verify(documentService, times(0)).deleteFiles(1,7);
    }

    @Test
    @DisplayName("Update Requirement persist infomation")
    public void updateRequirement_RequirementModified_RequirementUpdated(){
        RequirementDom requirementDom = new RequirementDom();
        UserDom userDom = mock(UserDom.class);
        requirementDom.setIdRequirement(1);
        requirementDom.setName("Test1");
        requirementDom.setIdentifier("12345");
        requirementDom.setDescription("description1");
        requirementDom.setPriority(Priority.BLOCKER);
        requirementDom.setComplexity(Complexity.HIGH);
        requirementDom.setState(State.APPROVED);
        requirementDom.setCost(27.0);
        requirementDom.setEstimatedHours(29.00);
        requirementDom.setStoryPoints(5);
        requirementDom.setSource("source1");
        requirementDom.setScope(Scope.PROJECT);
        requirementDom.setRisk(Risk.LOW);
        requirementDom.setCreated(date);
        requirementDom.setLastUpdated(date);
        requirementDom.setVersion("version1");
        requirementDom.setValidationMethod("validation1");
        requirementDom.setAuthor(userDom);
        requirementDom.setAssignedTo(userDom);
        requirementDom.setJustification("justification1");
        requirementDom.setTestCases("test cases1");
        requirementDom.setRequirementTypeId(1);
        requirementDom.setProjectId(1);

        Requirement requirement = createRequirement(true);
        User user = requirement.getAssignedTo();
        when(requirementRepository.findOne(1)).thenReturn(requirement);
        when(userRepository.findOne(requirementDom.getAssignedId())).thenReturn(user);
        when(requirementRepository.save(any(Requirement.class))).thenReturn(requirement);
        when(requirementConverter.toDomain(any(Requirement.class))).thenReturn(requirementDom);

        RequirementService requirementService = createRequirementService();
        RequirementDom obtained = requirementService.update(requirementDom,1,1);

        assertEquals(obtained,requirementDom);
        assertNotNull(obtained);
        verify(requirementRepository,times(1)).findOne(anyInt());
        verify(requirementRepository,times(1)).save(any(Requirement.class));
        verify(requirementConverter, times(1)).toDomain(any(Requirement.class));
        verify(userRepository, times(1)).findOne(anyInt());
    }


    private RequirementService createRequirementService(){
        return new RequirementService(objectRepository,requirementConverter,projectRepository,requirementRepository,
                                        reqTypeRepository, documentService, userRepository);
    }

    private Requirement createRequirement(boolean attributes) {
        Project project = new Project();
        project.setIdProject(1);

        RequirementType requirementType = new RequirementType();
        requirementType.setIdType(1);
        requirementType.setName("type");

        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);

        Requirement requirement = new Requirement();
        requirement.setIdRequirement(1);
        requirement.setName("Test");
        requirement.setIdentifier("1234");
        requirement.setDescription("description");
        requirement.setPriority(Priority.NORMAL);
        requirement.setComplexity(Complexity.NORMAL);
        requirement.setState(State.DRAFT);
        requirement.setCost(0.0);
        if(attributes){
            requirement.setEstimatedHours(27.00);
        }
        if(attributes){
            requirement.setStoryPoints(0);
        }
        requirement.setSource("source");
        requirement.setScope(Scope.FEATURE);
        requirement.setRisk(Risk.HIGH);
        requirement.setCreated(date);
        requirement.setLastUpdated(date);
        requirement.setVersion("version");
        requirement.setValidationMethod("validation");
        requirement.setJustification("justification");
        requirement.setTestCases("test cases");
        requirement.setObject(objectEntity);
        requirement.setRequirementType(requirementType);

        User user = mock(User.class);
        requirement.setAssignedTo(user);
        requirement.setAuthor(user);
        return requirement;
    }

    private RequirementDom createRequirementDom(boolean attributes) {
        RequirementDom requirement = new RequirementDom();
        requirement.setIdRequirement(1);
        requirement.setName("Test");
        requirement.setIdentifier("1234");
        requirement.setDescription("description");
        requirement.setPriority(Priority.NORMAL);
        requirement.setComplexity(Complexity.NORMAL);
        requirement.setState(State.DRAFT);
        requirement.setCost(0.0);
        if(attributes){
            requirement.setEstimatedHours(27.00);
        }
        else {
            requirement.setEstimatedHours(0.00);
        }
        if(attributes){
            requirement.setStoryPoints(0);
        }
        else {
            requirement.setStoryPoints(0);
        }
        requirement.setSource("source");
        requirement.setScope(Scope.FEATURE);
        requirement.setRisk(Risk.HIGH);
        requirement.setCreated(date);
        requirement.setLastUpdated(date);
        requirement.setVersion("version");
        requirement.setValidationMethod("validation");
        requirement.setJustification("justification");
        requirement.setTestCases("test cases");
        requirement.setRequirementTypeId(1);
        requirement.setProjectId(1);

        UserDom userDom = mock(UserDom.class);
        requirement.setAssignedTo(userDom);
        requirement.setAuthor(userDom);
        return requirement;
    }

}

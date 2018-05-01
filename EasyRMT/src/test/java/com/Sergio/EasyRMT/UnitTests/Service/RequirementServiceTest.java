/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Repository.*;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.Converter.RequirementConverter;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.EpicService;
import com.Sergio.EasyRMT.Service.RequirementService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.websocket.OnOpen;
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

    private Date date;
    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        requirementConverter = mock(RequirementConverter.class);
        projectRepository = mock(ProjectRepository.class);
        requirementRepository = mock(RequirementRepository.class);
        reqTypeRepository = mock(ReqTypeRepository.class);
        documentService = mock(DocumentService.class);
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
        Requirement requirement = mock(Requirement.class);
        RequirementDom requirementDom = mock(RequirementDom.class);

        when(requirementRepository.findOne(anyInt())).thenReturn(requirement);
        when(requirementConverter.toDomain(requirement)).thenReturn(requirementDom);

        RequirementService requirementService = createRequirementService();
        //Test conditions
        assertTrue(requirementService.getRequirement(7).equals(requirementDom));
        //Verify epicRepository has been called one time
        verify(requirementRepository,times(1)).findOne(7);
        //Verify epicConverter has been called one time
        verify(requirementConverter,times(1)).toDomain(requirement);
    }

    @Test
    @DisplayName("Create requirement persists an requirement from an requirement provided")
    public void createRequirement_RequirementProvided_ReturnsRequirementPersisted(){
        RequirementDom requirementDom = mock(RequirementDom.class);
        Requirement requirement = mock(Requirement.class);
        Project project = mock(Project.class);
        ObjectEntity objectEntity = mock(ObjectEntity.class);
        RequirementType requirementType = mock(RequirementType.class);

        when(projectRepository.findByIdProject(7)).thenReturn(Optional.of(project));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(requirementConverter.toModel(requirementDom)).thenReturn(requirement);
        when(objectEntity.getIdobject()).thenReturn(9);
        when(requirementRepository.save(requirement)).thenReturn(requirement);
        when(requirementConverter.toDomain(requirement)).thenReturn(requirementDom);
        when(reqTypeRepository.findByIdType(anyInt())).thenReturn(Optional.of(requirementType));
        when(requirementDom.getRequirementTypeId()).thenReturn(7);


        RequirementService requirementService = createRequirementService();
        RequirementDom obtained = requirementService.create(requirementDom,7);

        //Test conditions
        assertEquals(obtained,requirementDom);
        //Verify that epicConverter.toModel has been called once
        verify(requirementConverter, times(1)).toModel(requirementDom);
        //verify that projectRepository.save has been called once
        verify(projectRepository, times(1)).findByIdProject(7);
        //verify that epicConverter.toDomain has been called once
        verify(requirementConverter, times(1)).toDomain(requirement);
        //verify objectRepository has been called
        verify(objectRepository, times(1)).save(any(ObjectEntity.class));
        //verify requirement repository has been called
        verify(requirementRepository, times(1)).save(requirement);
        //verify requirement.setIdeEpic has been called
        verify(requirement, times(1)).setIdRequirement(9);
        //verify object entity. get id object has been called
        verify(objectEntity,times(1)).getIdobject();
        //verify requirement.setAuthor has been called
        verify(requirementDom, times(1)).setAuthor(anyInt());
        //Verify requirement created has been called
        verify(requirementDom, times(1)).setCreated(any(Date.class));
        //Verify requirement last updated has been called
        verify(requirementDom, times(1)).setLastUpdated(any(Date.class));
        verify(requirementDom, times(1)).getRequirementTypeId();
    }

    @Test
    @DisplayName("DeleteRequirement deletes a Requirement")
    public void deleteRequirement_IdObjectProvided_DeletesRequirement(){
        Requirement requirement = mock(Requirement.class);
        doReturn(true).doReturn(false).when(objectRepository).exists(anyInt());
        doNothing().when(objectRepository).deleteObject(anyInt());
        doReturn(false).when(requirementRepository).exists(anyInt());
        when(requirementRepository.findOne(anyInt())).thenReturn(requirement);

        RequirementService requirementService = createRequirementService();

        assertTrue(requirementService.deleteRequirement(7));
        //Verify that method calls two times to project repository.existsByIdProject
        verify(objectRepository, times(2)).exists(anyInt());
        //Verify that projectRepository.deleteProjectByIdProject() is called once
        verify(objectRepository, times(1)).deleteObject(anyInt());
        //verify requirement repository has been called
        verify(requirementRepository,times(1)).exists(anyInt());
    }

    @Test
    @DisplayName("Delete Requirement fails when epic does not exists in DB")
    public void deleteRequirement_IdObjectNotExist_ReturnFalse(){
        doReturn(false).when(objectRepository).exists(anyInt());

        RequirementService requirementService = createRequirementService();

        //Test conditions
        assertFalse(requirementService.deleteRequirement(7));
        //Verify that method calls one time to object repository.exists
        verify(objectRepository, times(1)).exists(anyInt());
        //Verify that epicRepository.findOne() is never called
        verify(requirementRepository, times(0)).exists(anyInt());
        //Verify that objectRepository.deleteObject() is never called
        verify(objectRepository, times(0)).deleteObject(anyInt());
    }

    @Test
    @DisplayName("Update Requirement persist infomation")
    public void updateRequirement_RequirementModified_RequirementUpdated(){
        RequirementDom requirementDom = new RequirementDom();
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
        requirementDom.setAuthor(0);
        requirementDom.setAssignedTo(1);
        requirementDom.setJustification("justification1");
        requirementDom.setTestCases("test cases1");
        requirementDom.setRequirementTypeId(1);
        requirementDom.setProjectId(1);

        Requirement requirement = createRequirement(true);
        when(requirementRepository.findOne(1)).thenReturn(requirement);
        when(requirementRepository.save(any(Requirement.class))).thenAnswer((Answer<Requirement>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Requirement) args[0];
        });
        //TODO Fix this test
        when(requirementConverter.toDomain(any(Requirement.class))).thenAnswer((Answer<RequirementDom>) invocation -> {
            Object[] args = invocation.getArguments();
            return (RequirementDom) args[0];
        });

        RequirementService requirementService = createRequirementService();
        RequirementDom obtained = requirementService.update(requirementDom,1,1);

        assertEquals(obtained,requirementDom);
        verify(requirementRepository,times(1)).findOne(anyInt());
        verify(requirementRepository,times(1)).save(any(Requirement.class));
        verify(requirementConverter, times(1)).toDomain(any(Requirement.class));
    }

    //TODO falta test para update todo null o igual??

    private RequirementService createRequirementService(){
        return new RequirementService(objectRepository,projectRepository,requirementConverter,requirementRepository,
                                        reqTypeRepository, documentService);
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
        requirement.setAuthor(0);
        if(attributes){
            requirement.setAssignedTo(27);
        }
        requirement.setJustification("justification");
        requirement.setTestCases("test cases");
        requirement.setObject(objectEntity);
        requirement.setRequirementType(requirementType);
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
        requirement.setAuthor(0);
        if(attributes){
            requirement.setAssignedTo(27);
        }
        else{
            requirement.setAssignedTo(0);
        }
        requirement.setJustification("justification");
        requirement.setTestCases("test cases");
        requirement.setRequirementTypeId(1);
        requirement.setProjectId(1);
        return requirement;
    }

}

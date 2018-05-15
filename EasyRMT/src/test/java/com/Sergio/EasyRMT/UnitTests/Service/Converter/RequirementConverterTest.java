/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.RequirementConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
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
public class RequirementConverterTest {
    @Mock
    private UserConverter userConverter;
    private Date date;
    @BeforeEach
    public void initDate(){
        userConverter = mock(UserConverter.class);
        date = new Date();
    }

    @Test
    @Description("Method toDomain receives a list of Requirements and returns a list of RequirementDom")
    public void toDomain_RequirementListProvided_RequirementDomListReturned(){
        Requirement requirement = createRequirement(true);
        requirement.setAssignedTo(null);
        List<Requirement> requirementList = new ArrayList<>();
        List<RequirementDom> expected = new ArrayList<>();
        User user = mock(User.class);
        UserDom userDom = mock(UserDom.class);
        requirement.setAuthor(user);
        requirement.setAssignedTo(user);

        when(userConverter.toDomain(any(User.class))).thenReturn(userDom);

        RequirementDom requirementDom = new RequirementDom();
        requirementDom.setIdRequirement(1);
        requirementDom.setName("Test");
        requirementDom.setIdentifier("1234");
        requirementDom.setAuthor(userDom);
        requirementDom.setAssignedTo(userDom);
        requirementDom.setRequirementTypeId(1);
        requirementDom.setProjectId(1);
        requirementDom.setState(State.DRAFT);

        requirementList.add(requirement);
        expected.add(requirementDom);

        RequirementConverter requirementConverter = createRequirementConverter();

        List<RequirementDom> obtained = requirementConverter.toDomain(requirementList);

        //TestConditions
        assertNotNull(obtained);
        assertEquals(1,obtained.size());
        assertFalse(obtained.isEmpty());
        assertEquals(obtained,expected);
        verify(userConverter, times(2)).toDomain(any(User.class));
    }

    @Test
    @Description("Method toDomain receives a Requirement with all attributes and returns a RequirementDom")
    public void toDomain_RequirementAllAttsProvided_RequirementDomReturned(){
        Requirement requirement = createRequirement(true);
        RequirementDom expected = createRequirementDom(true);
        User user = mock(User.class);
        UserDom userDom = mock(UserDom.class);
        requirement.setAuthor(user);
        requirement.setAssignedTo(user);
        expected.setAuthor(userDom);
        expected.setAssignedTo(userDom);
        when(userConverter.toDomain(any(User.class))).thenReturn(userDom);

        RequirementConverter requirementConverter = createRequirementConverter();

        RequirementDom obtained = requirementConverter.toDomain(requirement);
        //TestConditions
        assertNotNull(obtained);
        assertFalse(obtained == null);
        assertEquals(obtained,expected);
    }

    @Test
    @Description("Method toDomain receives a Requirement without assignedTo, estimated hours and storypoints" +
            " attributes and returns a  RequirementDom")
    public void toDomain_RequirementNotAllAttsProvided_RequirementDomReturned(){
        Requirement requirement = createRequirement(false);
        RequirementDom expected = createRequirementDom(false);
        User user = mock(User.class);
        UserDom userDom = mock(UserDom.class);
        requirement.setAuthor(user);
        requirement.setAssignedTo(user);
        expected.setAuthor(userDom);
        expected.setAssignedTo(userDom);
        when(userConverter.toDomain(any(User.class))).thenReturn(userDom);

        RequirementConverter requirementConverter = createRequirementConverter();

        RequirementDom obtained = requirementConverter.toDomain(requirement);
        //TestConditions
        assertNotNull(obtained);
        assertFalse(obtained == null);
        assertEquals(obtained,expected);
    }

    @Test
    @Description("Method toModel receives a RequirementDom with attributes and returns a Requirement")
    public void toModel_RequirementAllAttsProvided_RequirementReturned(){
        Requirement expected = createRequirement(true);
        expected.setIdRequirement(0);
        expected.setRequirementType(null);
        expected.setObject(null);
        RequirementDom requirementDom = createRequirementDom(true);

        RequirementConverter requirementConverter = createRequirementConverter();

        Requirement obtained = requirementConverter.toModel(requirementDom);
        //TestConditions
        assertNotNull(obtained);
        assertFalse(obtained == null);
        assertEquals(obtained,expected);
    }


    private RequirementConverter createRequirementConverter(){
        return new RequirementConverter(userConverter);
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
        return requirement;
    }

}

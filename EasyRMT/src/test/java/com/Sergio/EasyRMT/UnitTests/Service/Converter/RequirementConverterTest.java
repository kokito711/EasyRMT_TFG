/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.EpicDom;
import com.Sergio.EasyRMT.Domain.RequirementDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.EpicConverter;
import com.Sergio.EasyRMT.Service.Converter.RequirementConverter;
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
public class RequirementConverterTest {
    private Date date;
    @BeforeEach
    public void initDate(){
        date = new Date();
    }

    @Test
    @Description("Method toDomain receives a list of Requirements and returns a list of RequirementDom")
    public void toDomain_RequirementListProvided_RequirementDomListReturned(){
        Requirement requirement = createRequirement(true);
        List<Requirement> requirementList = new ArrayList<>();
        List<RequirementDom> expected = new ArrayList<>();

        RequirementDom requirementDom = new RequirementDom();
        requirementDom.setIdRequirement(1);
        requirementDom.setName("Test");
        requirementDom.setIdentifier("1234");
        requirementDom.setAuthor(0);
        requirementDom.setAssignedTo(27);
        requirementDom.setRequirementTypeId(1);
        requirementDom.setProjectId(1);

        requirementList.add(requirement);
        expected.add(requirementDom);

        RequirementConverter requirementConverter = createRequirementConverter();

        List<RequirementDom> obtained = requirementConverter.toDomain(requirementList);

        //TestConditions
        assertNotNull(obtained);
        assertTrue(obtained.toArray().length==1);
        assertFalse(obtained.isEmpty());
        assertEquals(obtained,expected);
    }

    @Test
    @Description("Method toDomain receives a Requirement with all attributes and returns a RequirementDom")
    public void toDomain_RequirementAllAttsProvided_RequirementDomReturned(){
        Requirement requirement = createRequirement(true);
        RequirementDom expected = createRequirementDom(true);

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
        return new RequirementConverter();
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

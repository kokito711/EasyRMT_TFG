package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.UseCaseDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.UseCaseConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UseCaseConverterTest {
    @Mock
    private UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        userConverter = mock(UserConverter.class);
    }

    @Test
    @DisplayName("Method toDomain receives a list of UseCase and returns a list of UseCaseDom")
    public void toDomain_UseCaseListProvided_UseCaseDomListReturned(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);
        Feature feature = mock(Feature.class);
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        when(feature.getIdFeature()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UseCase useCase = new UseCase();
        useCase.setIdUseCase(1);
        useCase.setName("UseCaseTest");
        useCase.setIdentifier("1234");
        useCase.setAuthor(user);
        useCase.setAssignedTo(user);
        useCase.setObject(objectEntity);
        useCase.setFeature(feature);
        useCase.setState(State.DRAFT);

        List<UseCaseDom> expected = new ArrayList<>();
        UseCaseDom useCaseDom = new UseCaseDom(
                1,
                "UseCaseTest",
                "1234",
                userDom,
                userDom,
                1,
                1
        );
        useCaseDom.setState(State.DRAFT);
        expected.add(useCaseDom);

        List<UseCase> useCaseList = new ArrayList<>();
        useCaseList.add(useCase);

        UseCaseConverter useCaseConverter = createUseCaseConverter();

        List<UseCaseDom> obtained = useCaseConverter.toDomain(useCaseList);

        //TestConditions
        assertNotNull(obtained);
        assertTrue(obtained.toArray().length==1);
        assertFalse(obtained.isEmpty());
        assertEquals(expected,obtained);
        //Verify userStoryConverter has been called
        verify(userConverter,times(2)).toDomain(user);

    }

    @Test
    @DisplayName("Method toDomain receives a UseCase and returns a UseCaseDom")
    public void toDomain_UseCaseProvided_UseCaseDomReturned() {
        Date date = new Date();
        UseCase useCase = new UseCase();
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = mock(Project.class);
        Feature feature = mock(Feature.class);

        useCase.setIdUseCase(1);
        useCase.setName("UseCase");
        useCase.setIdentifier("1234");
        useCase.setDescription("Description");
        useCase.setPriority(Priority.NORMAL);
        useCase.setComplexity(Complexity.NORMAL);
        useCase.setState(State.DRAFT);
        useCase.setCost(null);
        useCase.setEstimatedHours(null);
        useCase.setStoryPoints(null);
        useCase.setSource("Source");
        useCase.setScope(Scope.PROJECT);
        useCase.setRisk(Risk.HIGH);
        useCase.setCreated(date);
        useCase.setLastUpdated(date);
        useCase.setVersion("001");
        useCase.setValidationMethod("ValidationMethod");
        useCase.setJustification("Justification");
        useCase.setTestCases("TestCases");
        useCase.setAuthor(user);
        useCase.setAssignedTo(user);
        useCase.setObject(objectEntity);
        useCase.setActors("Actors");
        useCase.setPreconditions("Preconditions");
        useCase.setPostconditions("Postconditions");
        useCase.setSteps("Steps");
        useCase.setObject(objectEntity);
        useCase.setFeature(feature);

        objectEntity.setIdobject(1);
        objectEntity.setProject(project);

        when(project.getIdProject()).thenReturn(1);
        when(feature.getIdFeature()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UseCaseDom expected = new UseCaseDom(
                1,
                "UseCase",
                "1234",
                "Description",
                Priority.NORMAL,
                Complexity.NORMAL,
                State.DRAFT,
                0.0,
                0.0,
                0,
                "Source",
                Scope.PROJECT,
                Risk.HIGH,
                date,
                date,
                "001",
                "ValidationMethod",
                userDom,
                userDom,
                "Justification",
                "TestCases",
                "Actors",
                "Preconditions",
                "Postconditions",
                "Steps",
                1,
                1
        );

        UseCaseConverter useCaseConverter = createUseCaseConverter();

        UseCaseDom obtained = useCaseConverter.toDomain(useCase);

        Assert.assertNotNull(obtained);
        assertEquals(obtained, expected);
        //Verify userStoryConverter has been called
        verify(userConverter,times(2)).toDomain(user);
    }

    @Test
    @DisplayName("Method toModel receives a UseCaseDom and returns a UseCase")
    public void toModel_EpicDomProvided_EpicReturned() {
        UseCaseDom useCaseDom = new UseCaseDom();
        Date date = new Date();
        UseCase expected = new UseCase();
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(1);


        useCaseDom.setIdUseCase(1);
        useCaseDom.setName("UseCase");
        useCaseDom.setIdentifier("1234");
        useCaseDom.setDescription("Description");
        useCaseDom.setPriority(Priority.NORMAL);
        useCaseDom.setComplexity(Complexity.NORMAL);
        useCaseDom.setState(State.DRAFT);
        useCaseDom.setCost(null);
        useCaseDom.setEstimatedHours(0.0);
        useCaseDom.setStoryPoints(0);
        useCaseDom.setSource("Source");
        useCaseDom.setScope(Scope.PROJECT);
        useCaseDom.setRisk(Risk.HIGH);
        useCaseDom.setCreated(date);
        useCaseDom.setLastUpdated(date);
        useCaseDom.setVersion("001");
        useCaseDom.setValidationMethod("ValidationMethod");
        useCaseDom.setJustification("Justification");
        useCaseDom.setTestCases("TestCases");
        useCaseDom.setActors("Actors");
        useCaseDom.setPreconditions("Preconditions");
        useCaseDom.setPostconditions("Postconditions");
        useCaseDom.setSteps("Steps");

        expected.setIdUseCase(1);
        expected.setName("UseCase");
        expected.setIdentifier("1234");
        expected.setDescription("Description");
        expected.setPriority(Priority.NORMAL);
        expected.setComplexity(Complexity.NORMAL);
        expected.setState(State.DRAFT);
        expected.setCost(0.0);
        expected.setEstimatedHours(0.0);
        expected.setStoryPoints(0);
        expected.setSource("Source");
        expected.setScope(Scope.PROJECT);
        expected.setRisk(Risk.HIGH);
        expected.setCreated(date);
        expected.setLastUpdated(date);
        expected.setVersion("001");
        expected.setValidationMethod("ValidationMethod");
        expected.setJustification("Justification");
        expected.setTestCases("TestCases");
        expected.setActors("Actors");
        expected.setPreconditions("Preconditions");
        expected.setPostconditions("Postconditions");
        expected.setSteps("Steps");
        expected.setObject(objectEntity);


        UseCaseConverter useCaseConverter = createUseCaseConverter();

        UseCase obtained = useCaseConverter.toModel(useCaseDom);

        //asserts
        Assert.assertNotNull(obtained);
        assertEquals(obtained.getObject().getIdobject(), 1);
        Assert.assertNotNull(obtained.getObject().getTraced());
        assertTrue(obtained.getObject().getTraced().isEmpty());
        assertNull(obtained.getObject().getProject());
        assertEquals(expected,obtained);
    }

    private UseCaseConverter createUseCaseConverter(){
        return new UseCaseConverter(userConverter);
    }

}

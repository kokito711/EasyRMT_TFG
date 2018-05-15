package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Domain.UserStoryDom;
import com.Sergio.EasyRMT.Model.*;
import com.Sergio.EasyRMT.Model.types.*;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import com.Sergio.EasyRMT.Service.Converter.UserStoryConverter;
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
public class UserStoryConverterTest {
    @Mock
    private UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        userConverter = mock(UserConverter.class);
    }

    @Test
    @DisplayName("Method toDomain receives a list of UserStory and returns a list of UserStoryDom")
    public void toDomain_UserStoryListProvided_UserStoryDomListReturned(){
        Project project = new Project();
        project.setIdProject(1);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setProject(project);
        objectEntity.setIdobject(1);
        Epic epic = mock(Epic.class);
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        when(epic.getIdEpic()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UserStory useCase = new UserStory();
        useCase.setIdUserStory(1);
        useCase.setName("UseCaseTest");
        useCase.setIdentifier("1234");
        useCase.setAuthor(user);
        useCase.setAssignedTo(user);
        useCase.setObject(objectEntity);
        useCase.setEpic(epic);
        useCase.setState(State.DRAFT);

        List<UserStoryDom> expected = new ArrayList<>();
        UserStoryDom userStoryDom = new UserStoryDom(
                1,
                "UseCaseTest",
                "1234",
                userDom,
                userDom,
                1,
                1
        );
        userStoryDom.setState(State.DRAFT);
        expected.add(userStoryDom);

        List<UserStory> userStoryList = new ArrayList<>();
        userStoryList.add(useCase);

        UserStoryConverter userStoryConverter = createUserStoryConverter();

        List<UserStoryDom> obtained = userStoryConverter.toDomain(userStoryList);

        //TestConditions
        assertNotNull(obtained);
        assertTrue(obtained.toArray().length==1);
        assertFalse(obtained.isEmpty());
        assertEquals(expected,obtained);
        //Verify userStoryConverter has been called
        verify(userConverter,times(2)).toDomain(user);

    }

    @Test
    @DisplayName("Method toDomain receives a UserStory and returns a UserStoryDom")
    public void toDomain_UserStoryProvided_UserStoryDomReturned() {
        Date date = new Date();
        UserStory userStory = new UserStory();
        UserDom userDom = mock(UserDom.class);
        User user = mock(User.class);
        ObjectEntity objectEntity = new ObjectEntity();
        Project project = mock(Project.class);
        Epic epic = mock(Epic.class);

        userStory.setIdUserStory(1);
        userStory.setName("UseCase");
        userStory.setIdentifier("1234");
        userStory.setDescription("Description");
        userStory.setDefinitionOfDone("DoD");
        userStory.setPriority(Priority.NORMAL);
        userStory.setComplexity(Complexity.NORMAL);
        userStory.setState(State.DRAFT);
        userStory.setCost(null);
        userStory.setEstimatedHours(null);
        userStory.setStoryPoints(null);
        userStory.setSource("Source");
        userStory.setScope(Scope.PROJECT);
        userStory.setRisk(Risk.HIGH);
        userStory.setCreated(date);
        userStory.setLastUpdated(date);
        userStory.setVersion("001");
        userStory.setValidationMethod("ValidationMethod");
        userStory.setJustification("Justification");
        userStory.setTestCases("TestCases");
        userStory.setAuthor(user);
        userStory.setAssignedTo(user);
        userStory.setObject(objectEntity);
        userStory.setObject(objectEntity);
        userStory.setEpic(epic);

        objectEntity.setIdobject(1);
        objectEntity.setProject(project);

        when(project.getIdProject()).thenReturn(1);
        when(epic.getIdEpic()).thenReturn(1);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UserStoryDom expected = new UserStoryDom(
                1,
                "UseCase",
                "1234",
                "Description",
                "DoD",
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
                1,
                1
        );

        UserStoryConverter userStoryConverter = createUserStoryConverter();

        UserStoryDom obtained = userStoryConverter.toDomain(userStory);

        Assert.assertNotNull(obtained);
        assertEquals(obtained, expected);
        //Verify userStoryConverter has been called
        verify(userConverter,times(2)).toDomain(user);
    }

    @Test
    @DisplayName("Method toModel receives a UserStoryDom and returns a UserStory")
    public void toModel_EpicDomProvided_EpicReturned() {
        UserStoryDom useCaseDom = new UserStoryDom();
        Date date = new Date();
        UserStory expected = new UserStory();
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(1);


        useCaseDom.setIdUserStory(1);
        useCaseDom.setName("UseCase");
        useCaseDom.setIdentifier("1234");
        useCaseDom.setDescription("Description");
        useCaseDom.setDefinitionOfDone("DoD");
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

        expected.setIdUserStory(1);
        expected.setName("UseCase");
        expected.setIdentifier("1234");
        expected.setDescription("Description");
        expected.setDefinitionOfDone("DoD");
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
        expected.setObject(objectEntity);


        UserStoryConverter userStoryConverter = createUserStoryConverter();

        UserStory obtained = userStoryConverter.toModel(useCaseDom);

        //asserts
        Assert.assertNotNull(obtained);
        assertEquals(obtained.getObject().getIdobject(), 1);
        Assert.assertNotNull(obtained.getObject().getTraced());
        assertTrue(obtained.getObject().getTraced().isEmpty());
        assertNull(obtained.getObject().getProject());
        assertEquals(expected,obtained);
    }


    private UserStoryConverter createUserStoryConverter(){
        return new UserStoryConverter(userConverter);
    }
}

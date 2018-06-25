package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.TraceabilityController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.TraceDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.TraceabilityService;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.Principal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TraceabilityControllerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private TraceabilityService traceabilityService;
    @Mock
    private UserService userService;
    @Mock
    private CommonMethods commonMethods;
    @Mock
    private Principal principal;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        traceabilityService = mock(TraceabilityService.class);
        userService = mock(UserService.class);
        commonMethods = mock(CommonMethods.class);
        principal = mock(Principal.class);
    }

    @Test
    @DisplayName("deleteRelationship deletes relationship and returns a 200OK status")
    public void deleteRelationship_ProjectIdObj1IdObj2IdProvided_relationshipDeleted_200OKReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(traceabilityService.deleteRelatonship(1,2)).thenReturn(true);

        TraceabilityController traceabilityController = createTraceabilityController();
        ResponseEntity responseEntity = traceabilityController.deleteRelationship(1,1,2, principal);
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(1)).deleteRelatonship(1,2);
    }

    @Test
    @DisplayName("deleteRelationship fails to delete relationship and returns a INTERNAL_SERVER_ERROR status")
    public void deleteRelationship_ProjectIdObj1IdObj2IdProvided_relationshipDeleted_INTERNALSERVERERRORReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(traceabilityService.deleteRelatonship(1,2)).thenReturn(false);

        TraceabilityController traceabilityController = createTraceabilityController();
        ResponseEntity responseEntity = traceabilityController.deleteRelationship(1,1,2, principal);
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(1)).deleteRelatonship(1,2);
    }

    @Test
    @DisplayName("deleteRelationship user not allowed, Access denied exception")
    public void deleteRelationship_ProjectIdObj1IdObj2IdProvided_AccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        TraceabilityController traceabilityController = createTraceabilityController();
        try {
            traceabilityController.deleteRelationship(1,1,2, principal);
            fail("");
        }
        catch (AccessDeniedException e){
            assertEquals("Not allowed", e.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(0)).deleteRelatonship(1,2);
    }

    @Test
    @DisplayName("createRelationship creates relationship and returns a 200OK status")
    public void createRelationship_ProjectIdObj1IdObj2IdProvided_relationshipCreated_200OKReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        TraceDom newTraces = new TraceDom();
        newTraces.init();
        newTraces.getNewTraces().add("1");
        newTraces.getNewTraces().add("2");
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(traceabilityService.saveRelationship(1,newTraces)).thenReturn(true);

        TraceabilityController traceabilityController = createTraceabilityController();
        ResponseEntity responseEntity = traceabilityController.createRelationship(1,1,newTraces, principal);
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(1)).saveRelationship(1,newTraces);
    }

    @Test
    @DisplayName("createRelationship fails to create relationship and returns a INTERNAL_SERVER_ERROR status")
    public void createRelationship_ProjectIdObj1IdObj2IdProvided_relationshipDeleted_INTERNALSERVERERRORReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        TraceDom newTraces = new TraceDom();
        newTraces.init();
        newTraces.getNewTraces().add("1");
        newTraces.getNewTraces().add("2");
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(traceabilityService.saveRelationship(1,newTraces)).thenReturn(false);

        TraceabilityController traceabilityController = createTraceabilityController();
        ResponseEntity responseEntity = traceabilityController.createRelationship(1,1,newTraces, principal);
        assertNotNull(responseEntity);
        assertEquals("", responseEntity.getBody().toString());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(1)).saveRelationship(1,newTraces);
    }

    @Test
    @DisplayName("createRelationship user not allowed, Access denied exception")
    public void createRelationship_ProjectIdObj1IdObj2IdProvided_AccessDeniedException(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = mock(List.class);
        TraceDom newTraces = new TraceDom();
        newTraces.init();
        newTraces.getNewTraces().add("1");
        newTraces.getNewTraces().add("2");
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        TraceabilityController traceabilityController = createTraceabilityController();
        try {
            traceabilityController.createRelationship(1,1,newTraces, principal);
            fail("");
        }
        catch (AccessDeniedException e){
            assertEquals("Not allowed", e.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(traceabilityService, times(0)).saveRelationship(1,newTraces);
    }

    private TraceabilityController createTraceabilityController(){
        return new TraceabilityController(traceabilityService,userService,projectService,commonMethods);
    }

}

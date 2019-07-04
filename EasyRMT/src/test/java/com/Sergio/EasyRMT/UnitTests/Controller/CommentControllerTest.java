package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommentController;
import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Service.CommentService;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.security.access.AccessDeniedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommentControllerTest {
    @Mock
    private CommentService commentService;
    @Mock
    private CommonMethods commonMethods;
    @Mock
    private UserService userService;
    @Mock
    private ProjectService projectService;
    @Mock
    Principal principal;


    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        commentService = mock(CommentService.class);
        commonMethods = mock(CommonMethods.class);
        userService = mock(UserService.class);
        principal = mock(Principal.class);
    }

    @Test
    @DisplayName("add Comment saves a comment into data base")
    public void addComment_CommentSaved_200OKReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.createComment(any(CommentDom.class))).thenReturn(true);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.addComment(1,1,commentDom,principal);

        assertEquals(200,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(1)).setUser(user);
        verify(commentDom, times(1)).setObject(any(ObjectEntity.class));
        verify(commentService, times(1)).createComment(commentDom);
    }

    @Test
    @DisplayName("add Comment fails to save a comment into data base")
    public void addComment_CommentNotSaved_400BadRequestReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.createComment(any(CommentDom.class))).thenReturn(false);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.addComment(1,1,commentDom,principal);

        assertEquals(400,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(1)).setUser(user);
        verify(commentDom, times(1)).setObject(any(ObjectEntity.class));
        verify(commentService, times(1)).createComment(commentDom);
    }


    @Test
    @DisplayName("add Comment fails to save a comment into data base")
    public void addComment_CredentialsNotOk_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        CommentController commentController = createCommentController();

        try {
            commentController.addComment(1,1,commentDom,principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(0)).setUser(user);
        verify(commentDom, times(0)).setObject(any(ObjectEntity.class));
        verify(commentService, times(0)).createComment(commentDom);
    }

    @Test
    @DisplayName("update Comment saves a comment into data base")
    public void updateComment_CommentUpdated_200OKReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.updateComment(commentDom, user)).thenReturn(true);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.updateComment(1,1,commentDom,1,principal);

        assertEquals(200,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(1)).setIdComment(1);
        verify(commentService, times(1)).updateComment(commentDom,user);
    }

    @Test
    @DisplayName("update Comment fails to save a comment into data base")
    public void updateComment_CommentNotSaved_400BadRequestReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.updateComment(commentDom, user)).thenReturn(false);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.updateComment(1,1,commentDom,1,principal);

        assertEquals(400,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(1)).setIdComment(1);
        verify(commentService, times(1)).updateComment(commentDom,user);
    }


    @Test
    @DisplayName("update Comment fails to save a comment into data base")
    public void updateComment_CredentialsNotOk_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        CommentDom commentDom = mock(CommentDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        CommentController commentController = createCommentController();

        try {
            ResponseEntity response = commentController.updateComment(1,1,commentDom,1,principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentDom, times(0)).setIdComment(1);
        verify(commentService, times(0)).updateComment(commentDom,user);
    }

    @Test
    @DisplayName("delete Comment deletes a comment into data base")
    public void deleteComment_CommentUpdated_200OKReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.deleteComment(1, user)).thenReturn(true);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.deleteComment(1,1,1,principal);

        assertEquals(200,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentService, times(1)).deleteComment(1,user);
    }

    @Test
    @DisplayName("delete Comment fails to save a comment into data base")
    public void deleteComment_CommentNotSaved_400BadRequestReturned(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(commentService.deleteComment(1, user)).thenReturn(false);

        CommentController commentController = createCommentController();

        ResponseEntity response = commentController.deleteComment(1,1,1,principal);

        assertEquals(400,response.getStatusCodeValue());
        assertEquals("", response.getBody().toString());
        assertNotNull(response);
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentService, times(1)).deleteComment(1,user);
    }


    @Test
    @DisplayName("delete Comment fails to save a comment into data base")
    public void deleteComment_CredentialsNotOk_AccessDeniedExceptionThrown(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(projectDom);

        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        CommentController commentController = createCommentController();

        try {
            ResponseEntity response = commentController.deleteComment(1,1,1,principal);
            fail();
        }catch (AccessDeniedException exception){
            assertEquals("Not allowed",exception.getMessage());
        }
        verify(projectService, times(1)).getProject(1);
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(commentService, times(0)).deleteComment(1,user);
    }

    private CommentController createCommentController(){
        return new CommentController(
          commentService, commonMethods, userService, projectService
        );
    }

}

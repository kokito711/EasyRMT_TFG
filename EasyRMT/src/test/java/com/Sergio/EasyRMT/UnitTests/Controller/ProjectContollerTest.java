package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.ProjectController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.RequirementTypeDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.DocumentService;
import com.Sergio.EasyRMT.Service.ProjectService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectContollerTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private DocumentService documentService;
    @Mock
    private CommonMethods commonMethods;
    @Mock
    private UserService userService;
    @Mock
    Principal principal;
    @Mock
    BindingResult result;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        documentService = mock(DocumentService.class);
        commonMethods = mock(CommonMethods.class);
        userService = mock(UserService.class);
        principal = mock(Principal.class);
        result = mock(BindingResult.class);
    }

    @Test
    @DisplayName("createProjectView method returns a modelAndView object")
    public void createProjectView_MAVProvided_ReturnsMAV(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        List<RequirementTypeDom> requirementTypeDomList = mock(List.class);
        UserDom user = mock(UserDom.class);
        List<Group_user> groups = mock(List.class);
        when(projectService.getReqTypes()).thenReturn(requirementTypeDomList);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(user.getGroups()).thenReturn(groups);

        ModelAndView expected = new ModelAndView("createProject");
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", requirementTypeDomList);
        expected.addObject("user", "user");
        expected.addObject("groups", groups);
        expected.addObject("projectList", projectDomList);
        expected.addObject("isPM", true);


        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.createProjectView(new ModelAndView(), principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertNotNull(expected);
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("reqTypes"), expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("user"), expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("groups"), expected.getModel().get("groups"));
        assertEquals(obtained.getModel().get("isPM"), expected.getModel().get("isPM"));
        verify(projectService,times(1)).getReqTypes();
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("createProject method returns a redirect view")
    public void createProject_ProjectDomProvided_ReturnsRedirect(){
        ProjectDom projectDom = mock(ProjectDom.class);
        UserDom user = mock(UserDom.class);
        ModelAndView expected = new ModelAndView("redirect:/project/1");

        when(projectService.createProject(projectDom)).thenReturn(projectDom);
        when(projectDom.getIdProject()).thenReturn(1);
        when(result.hasErrors()).thenReturn(false);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.createProject(projectDom, principal, result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertTrue(obtained.getModel().isEmpty());
        verify(projectService,times(0)).getReqTypes();
        verify(userService, times(0)).findUser("user");
        verify(commonMethods, times(0)).getProjectsFromGroup(user);
        verify(commonMethods, times(0)).isPM(user, "user");
        verify(projectService,times(1)).createProject(projectDom);
    }

    @Test
    @DisplayName("createProject method returns a modelAndView object because errors on binding")
    public void createProject_ErrorsOnBinding_ReturnsMAV(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        List<RequirementTypeDom> requirementTypeDomList = mock(List.class);
        UserDom user = mock(UserDom.class);
        List<Group_user> groups = mock(List.class);
        when(projectService.getReqTypes()).thenReturn(requirementTypeDomList);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(result.hasErrors()).thenReturn(true);
        when(user.getGroups()).thenReturn(groups);

        ModelAndView expected = new ModelAndView("createProject");
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", requirementTypeDomList);
        expected.addObject("user", "user");
        expected.addObject("groups", groups);
        expected.addObject("projectList", projectDomList);
        expected.addObject("isPM", true);


        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.createProject(projectDom, principal,result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertNotNull(expected);
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("reqTypes"), expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("user"), expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("groups"), expected.getModel().get("groups"));
        assertEquals(obtained.getModel().get("isPM"), expected.getModel().get("isPM"));
        verify(projectService,times(1)).getReqTypes();
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(commonMethods, times(1)).isPM(user, "user");
        verify(projectService,times(0)).createProject(projectDom);
    }

    @Test
    @DisplayName("getUpdateView method returns a modelAndView object")
    public void updateProjectView_idProjectProvided_ReturnsMAV(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        List<RequirementTypeDom> requirementTypeDomList = mock(List.class);
        UserDom user = mock(UserDom.class);
        List<Group_user> groups = mock(List.class);
        when(projectService.getReqTypes()).thenReturn(requirementTypeDomList);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isPM(user, "user")).thenReturn(true);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(user.getGroups()).thenReturn(groups);

        ModelAndView expected = new ModelAndView("updateProject");
        expected.addObject("project", projectDom);
        expected.addObject("reqTypes", requirementTypeDomList);
        expected.addObject("user", "user");
        expected.addObject("groups", groups);
        expected.addObject("projectList", projectDomList);
        expected.addObject("isPM", true);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.getUpdateView(1,principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertNotNull(expected);
        assertEquals(obtained.getModel().get("project"), expected.getModel().get("project"));
        assertEquals(obtained.getModel().get("reqTypes"), expected.getModel().get("reqTypes"));
        assertEquals(obtained.getModel().get("projectList"), expected.getModel().get("projectList"));
        assertEquals(obtained.getModel().get("user"), expected.getModel().get("user"));
        assertEquals(obtained.getModel().get("groups"), expected.getModel().get("groups"));
        assertEquals(obtained.getModel().get("isPM"), expected.getModel().get("isPM"));
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(1)).getReqTypes();
        verify(commonMethods, times(1)).isPM(user, "user");
    }

    @Test
    @DisplayName("getUpdateView method returns throws an AccessDeniedException")
    public void updateProjectView_idProjectProvided_ThrowsAccessDeniedException(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        ProjectController projectController = createProjectController();

        try {
            projectController.getUpdateView(1,principal);
            fail();
        }
        catch (AccessDeniedException e){
            assertEquals(e.getMessage(), "Not allowed");
        }
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(0)).getReqTypes();
        verify(commonMethods, times(0)).isPM(user, "user");
    }

    @Test
    @DisplayName("updateProject method returns redirect")
    public void updateProject_idProjectProjectDomProvided_ReturnsRedirect(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(projectService.updateProject(1, projectDom)).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.updateProject(1,projectDom,principal);
        ModelAndView expected = new ModelAndView("redirect:/project/1");
        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertTrue(obtained.getModel().isEmpty());
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(1)).updateProject(1,projectDom);
    }

    @Test
    @DisplayName("updateProject method throws exception")
    public void updateProject_idProjectProjectDomProvided_ThrowsException(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        ProjectController projectController = createProjectController();

        try {
            projectController.updateProject(1,projectDom,principal);
            fail();
        }
        catch (AccessDeniedException e){
            assertEquals(e.getMessage(), "Not allowed");
        }

        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(0)).updateProject(1,projectDom);
    }

    @Test
    @DisplayName("deleteProject method returns an http.ok when project is deleted")
    public void deleteProject_idProjectProvided_ProjectDeleted_ReturnsOk(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(projectService.deleteProject(anyInt())).thenReturn(true);
        ResponseEntity expected = new ResponseEntity(HttpStatus.OK);

        ProjectController projectController = createProjectController();

        ResponseEntity obtained = projectController.deleteProject(1,principal);

        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(1)).deleteProject(1);
    }

    @Test
    @DisplayName("deleteProject method returns an http.Internal_Server_Error when project is not deleted")
    public void deleteProject_idProjectProvided_DeletionFails_ReturnsInternalServerError(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(true);
        when(projectService.deleteProject(anyInt())).thenReturn(false);
        ResponseEntity expected = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        ProjectController projectController = createProjectController();

        ResponseEntity obtained = projectController.deleteProject(1,principal);

        assertEquals(expected.getStatusCode(),obtained.getStatusCode());
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(1)).deleteProject(1);
    }

    @Test
    @DisplayName("deleteProject method throws an AccessDeniedException")
    public void deleteProject_idProjectProvided_DeletionFails_ThrowsException(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = new ProjectDom();
        UserDom user = mock(UserDom.class);
        when(projectService.getProject(1)).thenReturn(projectDom);
        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);
        when(commonMethods.isAllowed(projectDomList,projectDom)).thenReturn(false);

        ProjectController projectController = createProjectController();

        try {
            projectController.deleteProject(1,principal);
            fail();
        }
        catch (AccessDeniedException e){
            assertEquals(e.getMessage(), "Not allowed");
        }
        verify(userService, times(1)).findUser("user");
        verify(commonMethods, times(1)).getProjectsFromGroup(user);
        verify(projectService, times(1)).getProject(1);
        verify(commonMethods, times(1)).isAllowed(projectDomList,projectDom);
        verify(projectService,times(0)).deleteProject(1);
    }

    /*@Test
    @DisplayName("getProject method returns a modelAndView object")
    public void getProject_idProjectProvided_ReturnsMAV(){
        *//*ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        ProjectDom projectDom = mock(ProjectDom.class);

        ModelAndView expected = new ModelAndView("project");
        expected.addObject("projectList", projectDomList);
        expected.addObject("project", projectDom);
        when(projectService.getProjects()).thenReturn(projectDomList);
        when(projectService.getProject(7)).thenReturn(projectDom);

        ProjectController projectController = createProjectController();

        ModelAndView obtained = projectController.getProject(7);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertTrue(obtained.getModel().get("project").equals(expected.getModel().get("project")));
        assertTrue(obtained.getModel().get("projectList").equals(expected.getModel().get("projectList")));
        //Verify project service has been called
        verify(projectService,times(1)).getProjects();
        //Verify update project is called
        verify(projectService,times(1)).getProject(7);*//*
    }*/
    private ProjectController createProjectController(){
        return new ProjectController(projectService, documentService, commonMethods, userService);
    }
}

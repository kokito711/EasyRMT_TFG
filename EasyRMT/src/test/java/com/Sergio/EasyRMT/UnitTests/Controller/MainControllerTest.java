package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.MainController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

;

@RunWith(SpringJUnit4ClassRunner.class)
public class MainControllerTest {

    @Mock
    private
    ProjectService projectService;
    @Mock
    private
    UserService userService;
    @Mock
    private
    CommonMethods commonMethods;
    @Mock
    private
    Principal principal;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
        userService = mock(UserService.class);
        commonMethods = mock(CommonMethods.class);
        principal = mock(Principal.class);
    }

    @Test
    @DisplayName("dashboard method returns a modelAndView object")
    public void dashboard_MAVrequested_MAVReturned(){
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        UserDom userDom = mock(UserDom.class);

        ModelAndView expected = new ModelAndView("dashboard");
        expected.addObject("projectList", projectDomList);
        expected.addObject("isPM", true);

        when(principal.getName()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(userDom);
        when(commonMethods.isPM(userDom,"user")).thenReturn(true);
        when(commonMethods.getProjectsFromGroup(userDom)).thenReturn(projectDomList);

        MainController mainController = createMainController();

        ModelAndView obtained = mainController.dashboard(principal);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(expected.getModel().get("projectList"), obtained.getModel().get("projectList"));
        assertEquals(expected.getModel().get("isPM"), obtained.getModel().get("isPM"));
        verify(userService,times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(userDom);
        verify(commonMethods, times(1)).isPM(userDom, "user");
    }

    private MainController createMainController(){
        return new MainController(projectService, userService,commonMethods);
    }
}

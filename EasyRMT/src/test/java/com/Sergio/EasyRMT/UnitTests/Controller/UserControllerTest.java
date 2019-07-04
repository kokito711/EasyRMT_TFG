package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Controller.UserController;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private CommonMethods commonMethods;

    @BeforeEach
    public void initMocks(){
        userService = mock(UserService.class);
        commonMethods = mock(CommonMethods.class);
    }

    @Test
    @DisplayName("Request of login page returns login page")
    public void login_LoginViewRequested_LoginViewProvided(){
        assert true;
    }

    @Test
    @DisplayName("getUserProfile returns the user profile")
    public void getUserProfile_UsernameProvided_UserProfileViewReturned(){
        UserDom user = new UserDom();
        user.setUsername("user");
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        when(userService.findUser("user")).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);

        ModelAndView expected = new ModelAndView("user/profile");
        expected.addObject("userProf", user);
        expected.addObject("user", "user");
        expected.addObject("projectList", projectDomList);

        UserController userController = createUserController();
        ModelAndView obtained = userController.getUserProfile("user");

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(expected.getModel().get("userProf"), obtained.getModel().get("userProf"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("projectList"), obtained.getModel().get("projectList"));
        verify(userService,times(1)).findUser("user");
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
    }

    @Test
    @DisplayName("updateUserProfile returns the user profile")
    public void updateUserProfile_UsernameAndUserInfoProvided_UserProfileViewReturned(){
        UserDom user = new UserDom();
        BindingResult result = mock(BindingResult.class);
        user.setUsername("user");
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        when(userService.modifyUser(null,"user",user)).thenReturn(user);
        when(commonMethods.getProjectsFromGroup(user)).thenReturn(projectDomList);

        ModelAndView expected = new ModelAndView("user/profile");
        expected.addObject("success", true);
        expected.addObject("userProf", user);
        expected.addObject("user", "user");
        expected.addObject("projectList", projectDomList);

        UserController userController = createUserController();
        ModelAndView obtained = userController.updateUserProfile("user", user,result);

        //Test conditions
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(expected.getModel().get("success"), obtained.getModel().get("success"));
        assertEquals(expected.getModel().get("userProf"), obtained.getModel().get("userProf"));
        assertEquals(expected.getModel().get("user"), obtained.getModel().get("user"));
        assertEquals(expected.getModel().get("projectList"), obtained.getModel().get("projectList"));
        verify(userService,times(1)).modifyUser(null,"user",user);
        verify(commonMethods,times(1)).getProjectsFromGroup(user);
    }

    private UserController createUserController(){
        return new UserController(userService, commonMethods);
    }
}

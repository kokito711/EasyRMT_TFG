package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.AdminController;
import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.GroupService;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private GroupService groupService;
    @Mock
    private Principal principal;
    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void initMocks(){
        userService = mock(UserService.class);
        groupService = mock(GroupService.class);
        principal = mock(Principal.class);
        bindingResult = mock(BindingResult.class);
    }

    @Test
    @DisplayName("create user returns a view of create user")
    public void createUser_ReturnsMAV(){
        UserDom userDom = new UserDom();
        ModelAndView expected = new ModelAndView("admin/createUser");
        expected.addObject("user", userDom);

        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.createUser();

        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("user"), expected.getModel().get("user"));

    }

    @Test
    @DisplayName("create user creates an user")
    public void createUser_UserDomProvided_UserCreated(){
        UserDom userDom = mock(UserDom.class);
        when(userService.findUser("user")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).createUser(userDom);
        Locale locale = new Locale("es");
        ModelAndView expected = new ModelAndView("admin/createUser");
        expected.addObject("user", userDom);
        expected.addObject("success", true);

        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.createUser(userDom,bindingResult, locale);
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userProf"), expected.getModel().get("userProf"));
        assertEquals(obtained.getModel().get("success"), expected.getModel().get("success"));
        verify(userService, times(1)).createUser(userDom);
    }

    @Test
    @DisplayName("create user fails")
    public  void createUser_UserDomProvided_UserNotCreated(){
        UserDom userDom = mock(UserDom.class);
        when(userDom.getUsername()).thenReturn("user");
        when(userService.findUser("user")).thenReturn(userDom);
        List<FieldError> errors = generateFieldErrors();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(errors);
        Locale locale = new Locale("es_es");
        ModelAndView expected = new ModelAndView("admin/createUser");
        expected.addObject("user", userDom);
        expected.addObject("success", false);

        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.createUser(userDom,bindingResult, locale);
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userProf"), expected.getModel().get("userProf"));
        assertEquals(obtained.getModel().get("success"), expected.getModel().get("success"));
        verify(userService, times(0)).createUser(userDom);
    }

    @Test
    @DisplayName("getUserList returns a list with all users")
    public void getUserList_ReturnUserDomList(){
        List<UserDom> userList = mock(List.class);
        when(userService.getUsers()).thenReturn(userList);
        ModelAndView expected = new ModelAndView("admin/users");
        expected.addObject("userList", userList);
        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.getUserList();
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userList"), expected.getModel().get("userList"));
        verify(userService, times(1)).getUsers();
    }

    @Test
    @DisplayName("getUser returns a user")
    public void getUser_ReturnUserDom(){
        UserDom user = mock(UserDom.class);
        when(userService.findUserById(1)).thenReturn(user);
        ModelAndView expected = new ModelAndView("admin/userProfile");
        expected.addObject("userProf", user);
        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.getUser(1);
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userProf"), expected.getModel().get("userProf"));
        verify(userService, times(1)).findUserById(1);
    }

    @Test
    @DisplayName("Delete user deletes an user and returns a 200OK")
    public void deleteUser_UserIdProvided_UserDeleted(){
        when(userService.deleteUser(1)).thenReturn(true);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body("");
        AdminController adminController = createAdminController();
        ResponseEntity obtained = adminController.deleteUser(1);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    @DisplayName("Delete user fails to delete an user and returns an Internal Server Error")
    public void deleteUser_UserIdProvided_UserNotDeleted(){
        when(userService.deleteUser(1)).thenReturn(false);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        AdminController adminController = createAdminController();
        ResponseEntity obtained = adminController.deleteUser(1);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    @DisplayName("Edit user modifies a user and returns a model and view")
    public void editUser_UserIdAndUserProvided_UserModified(){
        UserDom user = mock(UserDom.class);
        when(userService.modifyUser(1, null, user)).thenReturn(user);
        ModelAndView expected = new ModelAndView("admin/userProfile");
        expected.addObject("userProf", user);
        expected.addObject("success", true);

        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.editUser(1, user,bindingResult);
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userProf"), expected.getModel().get("userProf"));
        assertEquals(obtained.getModel().get("success"), expected.getModel().get("success"));
        verify(userService, times(1)).modifyUser(1, null,user);
    }

    @Test
    @DisplayName("get group list returns a list with GroupDom objects")
    public void getGroupList_GroupDomListReturned(){
        List<GroupDom> groups = mock(List.class);
        when(groupService.findAll()).thenReturn(groups);
        ModelAndView expected = new ModelAndView("admin/groups");
        expected.addObject("groupList", groups);
        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.getGroupList();
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("groupList"), expected.getModel().get("groupList"));
        verify(groupService, times(1)).findAll();
    }

    @Test
    @DisplayName("getCreateGroup returns a model and view with page to create a group")
    public void getCreateGroup_MaVReturned(){
        List<UserDom> userList = mock(List.class);
        when(userService.getNoAdminUsers()).thenReturn(userList);
        ModelAndView expected = new ModelAndView("admin/createGroup");
        expected.addObject("userList", userList);
        expected.addObject("group", new GroupDom());
        AdminController adminController = createAdminController();
        ModelAndView obtained = adminController.getCreateGroup();
        assertNotNull(obtained);
        assertEquals(expected.getView(),obtained.getView());
        assertFalse(obtained.getModel().isEmpty());
        assertEquals(obtained.getModel().get("userList"), expected.getModel().get("userList"));
        assertEquals(obtained.getModel().get("group"), expected.getModel().get("group"));
        verify(userService, times(1)).getNoAdminUsers();
    }

    @Test
    @DisplayName("Modify user from group modifies a group")
    public void modifyGroup_GroupIdModificationTypeAndGroupDomProvided_200OkReturned(){
        GroupDom group = mock(GroupDom.class);
        doNothing().when(groupService).update(1,group, 1);
        doNothing().when(groupService).update(1,group, 2);
        doNothing().when(groupService).update(1,group, 3);
        AdminController adminController = createAdminController();
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body("");
        ResponseEntity obtained = adminController.modifyGroup(1,1,group,bindingResult);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(1)).update(1,group,1);
        obtained = adminController.modifyGroup(1,2,group,bindingResult);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(1)).update(1,group,2);
        obtained = adminController.modifyGroup(1,3,group,bindingResult);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(1)).update(1,group,3);
    }

    @Test
    @DisplayName("Modify user from group does not modify a group")
    public void modifyGroup_GroupIdModificationTypeAndGroupDomProvided_InternalServerErrorReturned(){
        GroupDom group = mock(GroupDom.class);
        AdminController adminController = createAdminController();
        ResponseEntity expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        ResponseEntity obtained = adminController.modifyGroup(1,5,group,bindingResult);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(0)).update(1,group,5);
    }

    @Test
    @DisplayName("remove user removes a user from a group provided")
    public void removeUser_GroupIdAndUserIdProvided_UserRemoved_200OKReturned(){
        when(groupService.removeUser(1,1)).thenReturn(true);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.OK).body("");
        AdminController adminController = createAdminController();
        ResponseEntity obtained = adminController.removeUser(1,1);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(1)).removeUser(1,1);
    }

    @Test
    @DisplayName("remove user removes a user from a group provided")
    public void removeUser_GroupIdAndUserIdProvided_UserRemoved_InternalServerErrorReturned(){
        when(groupService.removeUser(1,1)).thenReturn(false);
        ResponseEntity expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        AdminController adminController = createAdminController();
        ResponseEntity obtained = adminController.removeUser(1,1);
        assertNotNull(obtained);
        assertEquals(expected, obtained);
        verify(groupService, times(1)).removeUser(1,1);
    }

    private AdminController createAdminController(){
        return new AdminController(userService,groupService);
    }

    private List<FieldError> generateFieldErrors(){
        List<FieldError> errors = new ArrayList<>();
        FieldError error= new FieldError("username","username", "username");
        errors.add(error);
        error= new FieldError("email","email", "email");
        errors.add(error);
        error= new FieldError("password","password", "password");
        errors.add(error);
        error= new FieldError("name","name", "name");
        errors.add(error);
        error= new FieldError("lastName","lastName", "lastName");
        errors.add(error);
        error= new FieldError("phone","phone", "phone");
        errors.add(error);
        error= new FieldError("other","other", "other");
        errors.add(error);
        return errors;
    }
}

package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Role;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.RoleRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        userConverter = mock(UserConverter.class);
    }

    @Test
    @DisplayName("FindUser returns a user")
    public void findUser_UsernameProvided_UserReturned(){
        User user = new User();
        UserDom userDom = new UserDom();
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UserService userService = createUserService();
        assertEquals(userService.findUser("user"), userDom);
        verify(userRepository, times(1)).findByUsername("user");
        verify(userConverter, times(1)).toDomain(user);
    }

    @Test
    @DisplayName("FindUserbyId returns a user")
    public void findUserById_UserIdProvided_UserReturned(){
        User user = new User();
        UserDom userDom = new UserDom();
        when(userRepository.findOne(1)).thenReturn(user);
        when(userConverter.toDomain(user)).thenReturn(userDom);

        UserService userService = createUserService();
        assertEquals(userService.findUserById(1), userDom);
        verify(userRepository, times(1)).findOne(1);
        verify(userConverter, times(1)).toDomain(user);
    }

    @Test
    @DisplayName("Create user persists a user")
    public void createUser_UserProvided_UserCreated(){
        User user = new User();
        UserDom userDom = new UserDom();
        List<String> stringRoles = new ArrayList<>();
        Role role = new Role();
        stringRoles.add("ROLE1");
        userDom.setStringRoles(stringRoles);
        userDom.setPassword("Password");
        when(userConverter.toModel(userDom)).thenReturn(user);
        when(roleRepository.findByRole(anyString())).thenReturn(role);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("EncriptedPassword");
        when(userRepository.save(user)).thenReturn(user);

        UserService userService = createUserService();
        userService.createUser(userDom);
        verify(userConverter, times(1)).toModel(userDom);
        verify(roleRepository,times(1)).findByRole(anyString());
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("Get a list of users")
    public void getUsers_ReturnsList(){
        List<User> userList = mock(List.class);
        List<UserDom> userDomList = mock(List.class);
        doReturn(userList).when(userRepository).findAll();
        doReturn(userDomList).when(userConverter).toDomain(userList);

        UserService userService = createUserService();

        //Test conditions
        assertTrue(userService.getUsers().equals(userDomList));
        verify(userRepository, times(1)).findAll();
        verify(userConverter, times(1)).toDomain(userList);
    }

    @Test
    @DisplayName("Get a list of users that don't have an Admin role")
    public void getNoAdminUsers_ReturnsList(){
        List<User> userList = mock(List.class);
        List<UserDom> userDomList = mock(List.class);
        doReturn(userList).when(userRepository).findNotAdmin();
        doReturn(userDomList).when(userConverter).toDomain(userList);

        UserService userService = createUserService();

        //Test conditions
        assertEquals(userService.getNoAdminUsers(), userDomList);
        verify(userRepository, times(1)).findNotAdmin();
        verify(userConverter, times(1)).toDomain(userList);
    }

    @Test
    @DisplayName("Delete user deletes a user")
    public void deleteUser_IdUserProvided_DeletesUser(){
        doNothing().when(userRepository).deleteUser(1);
        doReturn(true).doReturn(false).when(userRepository).exists(1);

        UserService userService = createUserService();

        Assert.assertTrue(userService.deleteUser(1));
        verify(userRepository, Mockito.times(2)).exists(1);
        verify(userRepository, Mockito.times(1)).deleteUser(1);
    }

    @Test
    @DisplayName("Delete user fails to delete a user")
    public void deleteUser_IdUserProvided_DeletionFailed(){
        doNothing().when(userRepository).deleteUser(1);
        doReturn(true).doReturn(true).when(userRepository).exists(1);

        UserService userService = createUserService();

        Assert.assertFalse(userService.deleteUser(1));
        verify(userRepository, Mockito.times(2)).exists(1);
        verify(userRepository, Mockito.times(1)).deleteUser(1);
    }

    @Test
    @DisplayName("Delete user fails when user does not exists in DB")
    public void deleteUser_IdUserNotExist_ReturnFalse(){
        doReturn(false).when(userRepository).exists(1);

        UserService userService = createUserService();

        Assert.assertFalse(userService.deleteUser(1));
        verify(userRepository, Mockito.times(1)).exists(1);
        verify(userRepository, Mockito.times(0)).deleteUser(1);
    }

    @Test
    @DisplayName("Modify user modifies user using id")
    public void modifyUser_IdUserProvided_UserModified(){
        User user = createUser();
        UserDom expected = createUserDom();
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setRole("ROLE1");
        Role role2 = new Role();
        role2.setRole("ROLE2");
        roles.add(role1);
        roles.add(role2);

        when(userRepository.findOne(1)).thenReturn(user);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("Password1");
        when(roleRepository.findByRole("ROLE1")).thenReturn(role1);
        when(roleRepository.findByRole("ROLE2")).thenReturn(role2);
        when(userRepository.save(user)).thenReturn(user);
        when(userConverter.toDomain(user)).thenReturn(expected);

        UserService userService = createUserService();

        UserDom obtained = userService.modifyUser(1,null,expected);
        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(userRepository, times(1)).findOne(1);
        verify(userRepository, times(0)).findByUsername(anyString());
        verify(bCryptPasswordEncoder,times(1)).encode("Password1");
        verify(roleRepository, times(2)).findByRole(anyString());
        verify(userRepository, times(1)).save(user);
        verify(userConverter, times(1)).toDomain(user);
    }

    @Test
    @DisplayName("Modify user modifies user using username")
    public void modifyUser_UsernameProvided_UserModified(){
        User user = createUser();
        UserDom expected = createUserDom();
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setRole("ROLE1");
        Role role2 = new Role();
        role2.setRole("ROLE2");
        roles.add(role1);
        roles.add(role2);

        when(userRepository.findByUsername("User")).thenReturn(user);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("Password1");
        when(roleRepository.findByRole("ROLE1")).thenReturn(role1);
        when(roleRepository.findByRole("ROLE2")).thenReturn(role2);
        when(userRepository.save(user)).thenReturn(user);
        when(userConverter.toDomain(user)).thenReturn(expected);

        UserService userService = createUserService();

        UserDom obtained = userService.modifyUser(null,"User",expected);
        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(userRepository, times(0)).findOne(1);
        verify(userRepository, times(1)).findByUsername("User");
        verify(bCryptPasswordEncoder,times(1)).encode("Password1");
        verify(roleRepository, times(2)).findByRole(anyString());
        verify(userRepository, times(1)).save(user);
        verify(userConverter, times(1)).toDomain(user);
    }

    private UserService createUserService(){
        return new UserService(userRepository,roleRepository,bCryptPasswordEncoder,userConverter);
    }

    private UserDom createUserDom(){
        UserDom userDom = new UserDom();
        userDom.setPassword("Password1");
        userDom.setUsername("User");
        userDom.setUserId(1);
        userDom.setName("Name1");
        userDom.setLastName("LastName1");
        userDom.setPhone("Phone1");
        List<String> stringRoles = new ArrayList<>();
        stringRoles.add("ROLE1");
        stringRoles.add("ROLE2");
        userDom.setStringRoles(stringRoles);

        return userDom;
    }

    private User createUser(){
        User user = new User();
        user.setPassword("Password");
        user.setUsername("User");
        user.setUserId(1);
        user.setName("Name");
        user.setLastName("LastName");
        user.setPhone("Phone");
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRole("ROLE");
        roles.add(role);
        user.setRoles(roles);
        return user;
    }
}

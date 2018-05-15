package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.RoleDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.Role;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Service.Converter.RoleConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserConverterTest {

    @Mock
    private RoleConverter roleConverter;

    @BeforeEach
    public void initMocks(){
        roleConverter = mock(RoleConverter.class);
    }

    @Test
    @DisplayName("toDomain converts one User into one UserDom")
    public void toDomain_UserProvided_UserDomReturned(){
        List<Role> roleList = mock(List.class);
        List<RoleDom> roleDoms = mock(List.class);
        List<Group_user> groups = mock(List.class);
        User user = new User();
        user.setUserId(1);
        user.setUsername("Username");
        user.setPassword("Password");
        user.setEmail("Email");
        user.setName("User");
        user.setLastName("User");
        user.setPhone("Phone");
        user.setGroups(groups);
        user.setRoles(roleList);

        UserDom expected = new UserDom(
                1,
                "Username",
                "Email",
                "Password",
                "User",
                "User",
                "Phone",
                roleDoms
        );
        expected.setGroups(groups);

        when(roleConverter.toDomain(roleList)).thenReturn(roleDoms);

        UserConverter userConverter = createUserConverter();

        UserDom obtained = userConverter.toDomain(user);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(roleConverter, times(1)).toDomain(roleList);
    }

    @Test
    @DisplayName("toDomain converts a list of User into a list of UserDom")
    public void toDomain_UserListProvided_UserDomListReturned(){
        List<Role> roleList = mock(List.class);
        List<RoleDom> roleDoms = mock(List.class);
        List<Group_user> groups = mock(List.class);
        User user = new User();
        user.setUserId(1);
        user.setUsername("Username");
        user.setPassword("Password");
        user.setEmail("Email");
        user.setName("User");
        user.setLastName("User");
        user.setPhone("Phone");
        user.setGroups(groups);
        user.setRoles(roleList);

        UserDom userDom = new UserDom(
                1,
                "Username",
                "Email",
                "Password",
                "User",
                "User",
                "Phone",
                roleDoms
        );
        userDom.setGroups(groups);

        when(roleConverter.toDomain(roleList)).thenReturn(roleDoms);

        List<User> userList = new ArrayList<>();
        List<UserDom> expected = new ArrayList<>();
        userList.add(user);
        expected.add(userDom);

        UserConverter userConverter = createUserConverter();

        List<UserDom> obtained = userConverter.toDomain(userList);

        assertNotNull(obtained);
        assertFalse(obtained.isEmpty());
        assertEquals(1, obtained.size());
        assertEquals(expected,obtained);
        verify(roleConverter, times(1)).toDomain(roleList);
    }

    @Test
    @DisplayName("toModel converts one UserDom into one User")
    public void toDomain_UserDomProvided_UserReturned(){
        User expected = new User();
        expected.setUsername("Username");
        expected.setPassword("Password");
        expected.setEmail("Email");
        expected.setName("User");
        expected.setLastName("User");
        expected.setPhone("Phone");

        UserDom userDom = new UserDom();
        userDom.setUsername("Username");
        userDom.setPassword("Password");
        userDom.setEmail("Email");
        userDom.setName("User");
        userDom.setLastName("User");
        userDom.setPhone("Phone");

        UserConverter userConverter = createUserConverter();

        User obtained = userConverter.toModel(userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
    }

    private UserConverter createUserConverter(){
        return new UserConverter(roleConverter);
    }
}

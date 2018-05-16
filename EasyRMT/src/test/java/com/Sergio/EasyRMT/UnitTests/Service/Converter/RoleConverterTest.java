package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.RoleDom;
import com.Sergio.EasyRMT.Model.Role;
import com.Sergio.EasyRMT.Service.Converter.RoleConverter;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleConverterTest {
    @Test
    @DisplayName("ToDomain receives a role and returns a roleDom")
    public void toDomain_RoleReceived_RoleDomReturned(){
        Role role = createRole();
        RoleDom expected = createRoleDom();

        RoleConverter roleConverter = new RoleConverter();
        RoleDom obtained = roleConverter.toDomain(role);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
    }


    @Test
    @DisplayName("ToDomain receives a roleList and returns a roleDomList")
    public void toDomain_RoleListReceived_RoleDomListReturned(){
        Role role = createRole();
        RoleDom roleDom = createRoleDom();
        List<Role> roleList = new ArrayList<>();
        List<RoleDom> expected = new ArrayList<>();
        roleList.add(role);
        expected.add(roleDom);


        RoleConverter roleConverter = new RoleConverter();
        List<RoleDom> obtained = roleConverter.toDomain(roleList);

        assertNotNull(obtained);
        assertFalse(obtained.isEmpty());
        assertEquals(1, obtained.size());
        assertEquals(expected,obtained);
    }

    @Test
    @DisplayName("ToModel receives a roleDom and returns a role")
    public void toModel_RoleDomReceived_RoleReturned(){
        Role expected = new Role();
        expected.setRole("TestRole");
        expected.setRoleId(0);
        RoleDom roleDom = createRoleDom();

        RoleConverter roleConverter = new RoleConverter();
        Role obtained = roleConverter.toModel(roleDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
    }


    @Test
    @DisplayName("ToModel receives a roleDomSet and returns a roleSet")
    public void toModel_RoleDomSetReceived_RoleSetReturned(){
        Role role = new Role();
        role.setRole("TestRole");
        role.setRoleId(0);
        RoleDom roleDom = createRoleDom();
        Set<Role> expected = new HashSet<>();
        Set<RoleDom> roleDomList = new HashSet<>();
        expected.add(role);
        roleDomList.add(roleDom);


        RoleConverter roleConverter = new RoleConverter();
        Set<Role> obtained = roleConverter.toModel(roleDomList);

        assertNotNull(obtained);
        assertFalse(obtained.isEmpty());
        assertEquals(1, obtained.size());
        assertEquals(expected,obtained);
    }


    private RoleDom createRoleDom() {
        RoleDom roleDom = new RoleDom(1,"TestRole");
        return roleDom;
    }

    private Role createRole() {
        Role role = new Role();
        role.setRoleId(1);
        role.setRole("TestRole");
        return role;
    }
}

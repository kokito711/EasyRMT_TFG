package com.Sergio.EasyRMT.UnitTests.Controller;

import com.Sergio.EasyRMT.Controller.CommonMethods;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Group_UserKey;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommonMethodsTest {

    @Mock
    private ProjectService projectService;

    @BeforeEach
    public void initMocks(){
        projectService = mock(ProjectService.class);
    }

    @Test
    @DisplayName("isPM returns true")
    public void isPM_UserProvided_TrueReturned(){
        UserDom userDom = new UserDom();
        User user = new User();
        List<Group_user> groups = new ArrayList<>();
        Group_user group = mock(Group_user.class);
        user.setUsername("user");
        userDom.setGroups(groups);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group);
        when(group.isPM()).thenReturn(true);
        when(group.getPrimaryKey()).thenReturn(pk);
        when(pk.getUser()).thenReturn(user);

        CommonMethods commonMethods = createCommonMethodsComponent();
        assertTrue(commonMethods.isPM(userDom,"user"));

    }

    @Test
    @DisplayName("isPM returns false")
    public void isPM_UserProvided_FalseReturned(){
        UserDom userDom = new UserDom();
        User user = new User();
        List<Group_user> groups = new ArrayList<>();
        Group_user group = mock(Group_user.class);
        user.setUsername("user");
        userDom.setGroups(groups);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group);
        when(group.isPM()).thenReturn(false);
        when(group.getPrimaryKey()).thenReturn(pk);
        when(pk.getUser()).thenReturn(user);

        CommonMethods commonMethods = createCommonMethodsComponent();
        assertFalse(commonMethods.isPM(userDom,"user"));

    }

    @Test
    @DisplayName("getProjectsFromGroup returns a list of projectDom")
    public void getProjectsFromGroup_UserProvided_ProjectDomListReturned(){
        UserDom userDom = new UserDom();
        List<Group_user> groups = new ArrayList<>();
        Group_user group_user = mock(Group_user.class);
        userDom.setGroups(groups);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group_user);
        Group group = new Group();
        group.setGroup_id(1);
        when(group_user.getPrimaryKey()).thenReturn(pk);
        when(pk.getGroup()).thenReturn(group);
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);
        when(projectService.getProjects(1)).thenReturn(projectDomList);

        CommonMethods commonMethods = createCommonMethodsComponent();
        List<ProjectDom> obtained = commonMethods.getProjectsFromGroup(userDom);

        assertEquals(obtained,projectDomList);
        assertNotNull(obtained);
        verify(projectService, times(1)).getProjects(1);
    }

    @Test
    @DisplayName("isStakeholder returns true")
    public void isStakeholder_UserDomNameAndProjectProvided_ReturnsTrue(){
        UserDom userDom = new UserDom();
        User user = new User();
        user.setUsername("user");
        List<Group_user> groups = new ArrayList<>();
        Group_user group_user = mock(Group_user.class);
        userDom.setGroups(groups);
        Group group = new Group();
        group.setGroup_id(1);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group_user);
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);

        when(group_user.getPrimaryKey()).thenReturn(pk);
        when(pk.getGroup()).thenReturn(group);
        when(projectService.getProjects(1)).thenReturn(projectDomList);
        when(pk.getUser()).thenReturn(user);
        when(group_user.isStakeholder()).thenReturn(true);

        CommonMethods commonMethods = createCommonMethodsComponent();

        assertTrue(commonMethods.isStakeholder(userDom,"user", p1));
        verify(projectService, times(1)).getProjects(1);
    }

    @Test
    @DisplayName("isStakeholder user is not stakeholder")
    public void isStakeholder_UserDomNameAndProjectProvided_UserNotStakeholder_ReturnsFalse(){
        UserDom userDom = new UserDom();
        User user = new User();
        user.setUsername("user");
        List<Group_user> groups = new ArrayList<>();
        Group_user group_user = mock(Group_user.class);
        userDom.setGroups(groups);
        Group group = new Group();
        group.setGroup_id(1);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group_user);
        ProjectDom p1 = mock(ProjectDom.class);
        ProjectDom p2 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        projectDomList.add(p1);
        projectDomList.add(p2);

        when(group_user.getPrimaryKey()).thenReturn(pk);
        when(pk.getGroup()).thenReturn(group);
        when(projectService.getProjects(1)).thenReturn(projectDomList);
        when(pk.getUser()).thenReturn(user);
        when(group_user.isStakeholder()).thenReturn(false).thenReturn(false);

        CommonMethods commonMethods = createCommonMethodsComponent();

        assertFalse(commonMethods.isStakeholder(userDom,"user", p1));
        verify(projectService, times(1)).getProjects(1);
    }

    @Test
    @DisplayName("isStakeholder user is not assigned to any project")
    public void isStakeholder_UserDomNameAndProjectProvided_UserNotAssignedToProjects_ReturnsFalse(){
        UserDom userDom = new UserDom();
        List<Group_user> groups = new ArrayList<>();
        Group_user group_user = mock(Group_user.class);
        userDom.setGroups(groups);
        Group group = new Group();
        group.setGroup_id(1);
        Group_UserKey pk = mock(Group_UserKey.class);
        groups.add(group_user);
        ProjectDom p1 = mock(ProjectDom.class);
        List<ProjectDom> projectDomList = new ArrayList<>();
        when(group_user.getPrimaryKey()).thenReturn(pk);
        when(pk.getGroup()).thenReturn(group);
        when(projectService.getProjects(1)).thenReturn(projectDomList);

        CommonMethods commonMethods = createCommonMethodsComponent();

        assertFalse(commonMethods.isStakeholder(userDom,"user", p1));
        verify(projectService, times(1)).getProjects(1);
    }

    @Test
    @DisplayName("isStakeholder user is not assigned to any group")
    public void isStakeholder_UserDomNameAndProjectProvided_UserNotAssignedToGroups_ReturnsFalse(){
        UserDom userDom = new UserDom();
        List<Group_user> groups = new ArrayList<>();
        userDom.setGroups(groups);
        ProjectDom p1 = mock(ProjectDom.class);

        CommonMethods commonMethods = createCommonMethodsComponent();

        assertFalse(commonMethods.isStakeholder(userDom,"user", p1));
        verify(projectService, times(0)).getProjects(1);
    }

    private CommonMethods createCommonMethodsComponent(){
        return new CommonMethods(projectService);
    }
}

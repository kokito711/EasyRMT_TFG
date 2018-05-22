package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Group_UserKey;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.GroupRepository;
import com.Sergio.EasyRMT.Repository.GroupUserRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.GroupConverter;
import com.Sergio.EasyRMT.Service.GroupService;
import com.Sergio.EasyRMT.Service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GroupServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupConverter groupConverter;
    @Mock
    private GroupUserRepository groupUserRepository;

    @BeforeEach
    public void initMocks(){
        userService = mock(UserService.class);
        userRepository = mock(UserRepository.class);
        groupRepository = mock(GroupRepository.class);
        groupConverter = mock(GroupConverter.class);
        groupUserRepository = mock(GroupUserRepository.class);
    }

    @Test
    @DisplayName("FindAll method returns all existing groups")
    public void findAll_ReturnsListWithGroups(){
        List<Group> groups = mock(List.class);
        List<GroupDom> groupDomList = mock(List.class);
        when(groupRepository.findAll()).thenReturn(groups);
        when(groupConverter.toDomain(groups)).thenReturn(groupDomList);

        GroupService groupService = createGroupService();
        List<GroupDom> obtained = groupService.findAll();

        assertEquals(obtained, groupDomList);
        assertNotNull(obtained);
        verify(groupRepository, times(1)).findAll();
        verify(groupConverter, times(1)).toDomain(groups);
    }

    @Test
    @DisplayName("FindGroup method returns existing group")
    public void findAll_GroupIdProvided_ReturnsGroup(){
        Group group = mock(Group.class);
        GroupDom groupDom = mock(GroupDom.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(groupConverter.toDomain(group)).thenReturn(groupDom);

        GroupService groupService = createGroupService();
        GroupDom obtained = groupService.findGroup(1);

        assertEquals(obtained, groupDom);
        assertNotNull(obtained);
        verify(groupRepository, times(1)).findOne(1);
        verify(groupConverter, times(1)).toDomain(group);
    }

    @Test
    @DisplayName("Delete user from group deletes user from group")
    public void deleteUser_GroupIdAndUserIdProvided_UserDeletedFromGroup(){
        Group group = mock(Group.class);
        User user = mock(User.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(userRepository.findOne(1)).thenReturn(user);
        doNothing().when(groupUserRepository).delete(any(Group_UserKey.class));
        when(groupUserRepository.exists(any(Group_UserKey.class))).thenReturn(false);

        GroupService groupService = createGroupService();
        assertTrue(groupService.removeUser(1,1));
        verify(groupRepository, times(1)).findOne(1);
        verify(userRepository,times(1)).findOne(1);
        verify(groupUserRepository, times(1)).delete(any(Group_UserKey.class));
        verify(groupUserRepository, times(1)).exists(any(Group_UserKey.class));
    }

    @Test
    @DisplayName("Delete user from group fails to delete user from group")
    public void deleteUser_GroupIdAndUserIdProvided_UserDeletionFailed(){
        Group group = mock(Group.class);
        User user = mock(User.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(userRepository.findOne(1)).thenReturn(user);
        doNothing().when(groupUserRepository).delete(any(Group_UserKey.class));
        when(groupUserRepository.exists(any(Group_UserKey.class))).thenReturn(true);

        GroupService groupService = createGroupService();
        assertFalse(groupService.removeUser(1,1));
        verify(groupRepository, times(1)).findOne(1);
        verify(userRepository,times(1)).findOne(1);
        verify(groupUserRepository, times(1)).delete(any(Group_UserKey.class));
        verify(groupUserRepository, times(1)).exists(any(Group_UserKey.class));
    }

    @Test
    @DisplayName("Create group creates a group")
    public void createGroup_GroupDomProvided_GroupCreated(){
        Group group = mock(Group.class);
        GroupDom groupDom = mock(GroupDom.class);
        when(groupConverter.toModel(groupDom)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        User user = mock(User.class);
        when(groupDom.getPm()).thenReturn(1);
        when(userRepository.findOne(1)).thenReturn(user);
        List<String> users = new ArrayList<>();
        users.add("User1");
        List<String> stakeholders = new ArrayList<>();
        stakeholders.add("User2");
        when(groupDom.getStringUsers()).thenReturn(users);
        when(groupDom.getStakeholders()).thenReturn(stakeholders);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(groupUserRepository.save(any(List.class))).thenReturn(mock(List.class));

        GroupService groupService = createGroupService();
        groupService.createGroup(groupDom);
        verify(groupConverter,times(1)).toModel(groupDom);
        verify(groupRepository, times(1)).save(group);
        verify(userRepository, times(1)).findOne(1);
        verify(userRepository, times(2)).findByUsername(anyString());
        verify(groupUserRepository, times(1)).save(any(List.class));
    }

    @Test
    @DisplayName("Update group updates group part 1")
    public void updateGroup_GroupAndPartProvided_GroupPart1Updated(){
        Group group = mock(Group.class);
        GroupDom groupDom = mock(GroupDom.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(group.getName()).thenReturn("Group");
        when(groupDom.getName()).thenReturn("Group1");
        List<Group_user> group_users = new ArrayList<>();
        Group_UserKey userKey = new Group_UserKey();
        User user = new User();
        user.setUserId(1);
        userKey.setUser(user);
        userKey.setGroup(group);
        Group_user group_user = new Group_user();
        group_user.setPrimaryKey(userKey);
        group_user.setPM(true);
        group_users.add(group_user);
        when(group.getGroup()).thenReturn(group_users);
        when(groupDom.getPm()).thenReturn(2);
        doNothing().when(groupUserRepository).delete(userKey);
        when(userRepository.findOne(2)).thenReturn(user);
        when(groupUserRepository.save(any(Group_user.class))).thenReturn(group_user);

        GroupService groupService = createGroupService();
        groupService.update(1,groupDom,1);
        verify(groupRepository, times(1)).findOne(1);
        verify(groupUserRepository, times(1)).delete(userKey);
        verify(userRepository, times(1)).findOne(2);
        verify(groupUserRepository, times(1)).save(any(Group_user.class));
    }

    @Test
    @DisplayName("Update group updates group part 2")
    public void updateGroup_GroupAndPartProvided_GroupPart2Updated(){
        List<String> users = new ArrayList<>();
        users.add("User1");
        User user = mock(User.class);
        Group group = mock(Group.class);
        GroupDom groupDom = mock(GroupDom.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(userRepository.findByUsername("User1")).thenReturn(user);
        when(groupUserRepository.save(any(Group_user.class))).thenReturn(mock(Group_user.class));
        when(groupDom.getStringUsers()).thenReturn(users);

        GroupService groupService = createGroupService();
        groupService.update(1,groupDom,2);
        verify(groupRepository, times(1)).findOne(1);
        verify(userRepository, times(1)).findByUsername("User1");
        verify(groupUserRepository, times(1)).save(anyList());
    }

    @Test
    @DisplayName("Update group updates group part 3")
    public void updateGroup_GroupAndPartProvided_GroupPart3Updated(){
        List<String> users = new ArrayList<>();
        users.add("User1");
        User user = mock(User.class);
        Group group = mock(Group.class);
        GroupDom groupDom = mock(GroupDom.class);
        when(groupRepository.findOne(1)).thenReturn(group);
        when(userRepository.findByUsername("User1")).thenReturn(user);
        when(groupUserRepository.save(any(Group_user.class))).thenReturn(mock(Group_user.class));
        when(groupDom.getStakeholders()).thenReturn(users);

        GroupService groupService = createGroupService();
        groupService.update(1,groupDom,3);
        verify(groupRepository, times(1)).findOne(1);
        verify(userRepository, times(1)).findByUsername("User1");
        verify(groupUserRepository, times(1)).save(anyList());
    }

    private GroupService createGroupService(){
        return new GroupService(userService,userRepository,groupRepository,
                groupConverter,groupUserRepository);
    }
}

package com.Sergio.EasyRMT.UnitTests.Service.Converter;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.Converter.GroupConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GroupConverterTest {

    @BeforeEach
    public void initMocks(){

    }

    @Test
    @DisplayName("ToDomain converts a list of Group into a list of GroupDom")
    public void toDomain_GroupListProvided_GroupDomListObtained(){
        List<Group_user> group_user = mock(List.class);
        Group group = mock(Group.class);
        List<Group> groupList = new ArrayList<>();
        groupList.add(group);

        when(group.getGroup_id()).thenReturn(1);
        when(group.getName()).thenReturn("MockGroup");
        when(group.getGroup()).thenReturn(group_user);

        GroupConverter groupConverter = new GroupConverter();
        List<GroupDom> groupDoms = groupConverter.toDomain(groupList);
        assertNotNull(groupDoms);
        assertFalse(groupDoms.isEmpty());
        assertEquals(1,groupDoms.size());
        verify(group, times(1)).getGroup_id();
        verify(group,times(1)).getName();
        verify(group, times(1)).getGroup();
    }

    @Test
    @DisplayName("ToDomain converts a Group into a GroupDom")
    public void toDomain_GroupProvided_GroupDomObtained(){
        List<Group_user> group_user = mock(List.class);
        Group group = mock(Group.class);

        when(group.getGroup_id()).thenReturn(1);
        when(group.getName()).thenReturn("MockGroup");
        when(group.getGroup()).thenReturn(group_user);

        GroupConverter groupConverter = new GroupConverter();
        GroupDom groupDom = groupConverter.toDomain(group);
        assertNotNull(groupDom);
        verify(group, times(1)).getGroup_id();
        verify(group,times(1)).getName();
        verify(group, times(1)).getGroup();
    }

    @Test
    @DisplayName("ToModel converts a GroupDom into a Group")
    public void toModel_GroupDomProvided_GroupObtained(){
        GroupDom groupDom = mock(GroupDom.class);

        when(groupDom.getName()).thenReturn("MockGroup");

        GroupConverter groupConverter = new GroupConverter();
        Group group = groupConverter.toModel(groupDom);

        assertNotNull(groupDom);
        verify(groupDom,times(1)).getName();

    }
}

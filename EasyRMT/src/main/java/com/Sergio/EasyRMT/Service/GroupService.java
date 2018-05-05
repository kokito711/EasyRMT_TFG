/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Group_UserKey;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.GroupRepository;
import com.Sergio.EasyRMT.Repository.GroupUserRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.GroupConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private UserService userService;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupConverter groupConverter;
    private GroupUserRepository groupUserRepository;

    @Autowired
    public GroupService(UserService userService, UserRepository userRepository, GroupRepository groupRepository,
                        GroupConverter groupConverter, GroupUserRepository groupUserRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
        this.groupUserRepository = groupUserRepository;
    }

    /**
     * This method find all groups in db and returns a list with them
     * @return list with all existing groups in db
     */
    @Transactional(readOnly = true)
    public List<GroupDom> findAll() {
        List<Group> groups = groupRepository.findAll();
        List<GroupDom> groupDomList = groupConverter.toDomain(groups);
        return groupDomList;
    }

    /**
     * This method receives a groupId and search it in database. Then return the found group.
     * @param groupId
     * @return found group
     */
    public GroupDom findGroup(int groupId) {
        Group group = groupRepository.findOne(groupId);
        GroupDom groupDom = groupConverter.toDomain(group);
        return groupDom;
    }

    /**
     * This method receives a group from controller and persist the information
     * @param groupDom information to be persisted
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGroup(GroupDom groupDom) {
        //Group creation
        Group group = groupConverter.toModel(groupDom);
        group = groupRepository.save(group);

        //Obtaining users and filling a list of Group_user with them
        List<Group_user> group_users = new ArrayList<>();
        //First add the pm
        User user = userRepository.findOne(groupDom.getPm());
        Group_user groupUser = new Group_user(user,group,true,false);
        group_users.add(groupUser);
        //Second --> add the analyst
        addAnalyst(groupDom.getStringUsers(), group_users, group);
        //Third--> add stakeholders
        addStakeholder(groupDom.getStakeholders(), group_users, group);
        //Fourth--> persist relationship
        groupUserRepository.save(group_users);
    }

    /**
     * This method obtains information about one group and tries to update it
     * @param groupId id of group to be updated
     * @param groupDom info to update
     * @param groupPart part of group to be updated (1: name and PM, 2 Analysts, 3 Stakeholders)
     * @return GroupDom with updated group
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void update(int groupId, GroupDom groupDom, int groupPart) {
        Group group = groupRepository.findOne(groupId);
        Group persisted = group;
        switch (groupPart){
            case 1:{
                if(!group.getName().equals(groupDom.getName())){
                    group.setName(groupDom.getName());
                }
                for(Group_user user: group.getGroup()){
                    if(user.isPM()){
                        if(!(user.getPrimaryKey().getUser().getUserId() == groupDom.getPm())){
                            groupUserRepository.delete(user.getPrimaryKey());
                            Group_user group_user = new Group_user(
                                    userRepository.findOne(groupDom.getPm()),
                                    user.getPrimaryKey().getGroup(),
                                    true,
                                    false
                            );
                            groupUserRepository.save(group_user);
                        }
                        break;
                    }
                }
                break;
            }
            case 2:{
                List<Group_user> users = new ArrayList<>();
                for (String userS : groupDom.getStringUsers()){
                    User user = userRepository.findByUsername(userS);
                    Group_user group_user = new Group_user(user, group, false,false);
                    users.add(group_user);
                }
                groupUserRepository.save(users);
                break;
            }
            case 3:{
                List<Group_user> users = new ArrayList<>();
                for (String userS : groupDom.getStakeholders()){
                    User user = userRepository.findByUsername(userS);
                    Group_user group_user = new Group_user(user, group, false,true);
                    users.add(group_user);
                }
                groupUserRepository.save(users);
                break;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeUser(int groupId, int userId) {
        Group group = groupRepository.findOne(groupId);
        User user = userRepository.findOne(userId);
        Group_UserKey key = new Group_UserKey();
        key.setGroup(group);
        key.setUser(user);
        groupUserRepository.delete(key);
        if(groupUserRepository.exists(key)){
            return false;
        }
        return true;
    }

    private void addAnalyst(List<String> stringUsers, List<Group_user> group_users, Group group) {
        for (String userS : stringUsers){
            User user = userRepository.findByUsername(userS);
            Group_user group_user = new Group_user(user, group, false,false);
        }
    }

    private void addStakeholder(List<String> stakeholders, List<Group_user> group_users, Group group) {
        for (String userS : stakeholders){
            User user = userRepository.findByUsername(userS);
            Group_user group_user = new Group_user(user, group, false,true);
        }
    }
}

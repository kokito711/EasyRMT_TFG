/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Role;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.RoleRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userConverter = userConverter;
    }

    /**
     * This method search in db for a user passing username to query as filter.
     * @param username username to be found
     * @return {@link UserDom} user with related username
     */
    @Transactional(readOnly = true)
    public UserDom findUser(String username){
        User user = userRepository.findByUsername(username);
        if(user != null) {
            UserDom userDom = userConverter.toDomain(user);
            return userDom;
        }
        else{
            return null;
        }
    }

    /**
     * This method search in db for a user passing user_id to query as filter.
     * @param userId userId to be found
     * @return {@link UserDom} user with related username
     */
    @Transactional(readOnly = true)
    public UserDom findUserById(int userId) {
        User user = userRepository.findOne(userId);
        if(user != null) {
            UserDom userDom = userConverter.toDomain(user);
            return userDom;
        }
        else{
            return null;
        }
    }

    /**
     * This method persists an user received from domain and converted in db model.
     * @param userDom user to be persisted
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDom userDom){
        User user = userConverter.toModel(userDom);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRole(userDom.getStringRoles().get(0));
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(userDom.getPassword()));
        userRepository.save(user);
    }

    /**
     * This method obtains a list with all existing users in db
     * @return List with existin users
     */
    @Transactional(readOnly = true)
    public List<UserDom> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDom> userDomList = userConverter.toDomain(userList);
        return userDomList;
    }

    /**
     * This method deletes an user
     * @param userId
     * @return true if deleted, false if not
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(int userId) {
        if(userRepository.exists(userId)){
            userRepository.deleteUser(userId);
            if (userRepository.exists(userId)){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * This method tries to update an user if user has been modified
     * @param userId id of user to be modified
     * @param userInfo information to modify
     * @return user modified if there is any change or userInfo in no changes
     */
    @Transactional(rollbackFor = Exception.class)
    public UserDom modifyUser(@Nullable Integer userId, @Nullable String username, UserDom userInfo) {
        User user = new User();
        if(userId != null) {
            user = userRepository.findOne(userId);
        }
        else if (username != null){
            user = userRepository.findByUsername(username);
        }
        else{
            return userInfo;
        }
        boolean modified = false;
        if(userInfo.getPassword()!= "") {
            String password = bCryptPasswordEncoder.encode(userInfo.getPassword());
            if(!user.getPassword().equals(password)){
                user.setPassword(password);
                modified = true;
            }
        }
        if(userInfo.getEmail()!= null && !user.getEmail().equals(userInfo.getEmail())){
            user.setEmail(userInfo.getEmail());
            modified = true;
        }
        if (userInfo.getName()!= null && !user.getName().equals(userInfo.getName())){
            user.setName(userInfo.getName());
            modified = true;
        }
        if (userInfo.getLastName()!= null && !user.getLastName().equals(userInfo.getLastName())){
            user.setLastName(userInfo.getLastName());
            modified = true;
        }
        if (userInfo.getPhone()!= null && !user.getPhone().equals(userInfo.getPhone())){
            user.setPhone(userInfo.getPhone());
            modified = true;
        }
        if(userInfo.getStringRoles()!=null) {
            List<Role> roleListPersisted = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roleListPersisted.add(role);
            }
            List<Role> roleList = new ArrayList<>();
            for (String role : userInfo.getStringRoles()) {
                if (role != "NONE") {
                    roleList.add(roleRepository.findByRole(role));
                }
            }
            if (!roleListPersisted.equals(roleList)) {
                Set<Role> roleSet = new HashSet<>();
                for (Role role : roleList) {
                    roleSet.add(role);
                }
                user.setRoles(roleSet);
                modified = true;
            }
        }
        if (modified){
            user = userRepository.save(user);
            userInfo = userConverter.toDomain(user);
        }
    return userInfo;
    }

    @Transactional(readOnly = true)
    public List<UserDom> getNoAdminUsers() {
        List<User> users = userRepository.findNotAdmin();
        List<UserDom> userDomList = userConverter.toDomain(users);
        return userDomList;
    }
}

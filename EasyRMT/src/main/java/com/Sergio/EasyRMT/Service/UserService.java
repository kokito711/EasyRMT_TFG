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
}

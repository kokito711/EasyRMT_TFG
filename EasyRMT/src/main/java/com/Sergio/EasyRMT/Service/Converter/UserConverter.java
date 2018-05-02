/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.RoleDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Role;
import com.Sergio.EasyRMT.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserConverter {
    private RoleConverter roleConverter;

    @Autowired
    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    public UserDom toDomain(User user) {
        Set<RoleDom> roleDoms = new HashSet<>();
        if(user.getRoles() != null) {
           roleDoms = roleConverter.toDomain(user.getRoles());
        }
        UserDom userDom = new UserDom(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
        if (user.getName()!=null) {
            userDom.setName(user.getName());
        }
        if (user.getLastName()!=null){
            userDom.setLastName(user.getLastName());
        }
        if (user.getPhone()!= null){
            userDom.setPhone(user.getPhone());
        }
        userDom.setRoles(roleDoms);
        return userDom;
    }

    public List<UserDom> toDomain(List<User> users){
        List<UserDom> userDoms = new ArrayList<>();
        for (User user :users){
            userDoms.add(toDomain(user));
        }
        return userDoms;
    }

    public User toModel(UserDom userDom){
        User user = new User();
        user.setUsername(userDom.getUsername());
        user.setEmail(userDom.getEmail());
        user.setPassword(userDom.getPassword());
        if (userDom.getName()!=null) {
            user.setName(userDom.getName());
        }
        if (userDom.getLastName()!=null){
            user.setLastName(userDom.getLastName());
        }
        if (userDom.getPhone()!= null){
            user.setPhone(userDom.getPhone());
        }
        return user;
    }

    public List<User> toModel(List<UserDom> userDoms){
        List<User> users = new ArrayList<>();
        for (UserDom userDom :userDoms){
            users.add(toModel(userDom));
        }
        return users;
    }
}

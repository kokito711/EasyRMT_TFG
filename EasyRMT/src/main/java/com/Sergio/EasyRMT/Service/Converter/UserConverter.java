/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.RoleDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {
    private RoleConverter roleConverter;

    @Autowired
    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    /**
     * This method converts a {@link User} (Model) to a {@link UserDom} (Domain)
     * @param user {@link User}
     * @return {@link UserDom}
     */
    public UserDom toDomain(User user) {
        List<RoleDom> roleDoms = new ArrayList<>();
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
        if(user.getGroups()!=null){
            userDom.setGroups(user.getGroups());
        }
        userDom.setRoles(roleDoms);
        return userDom;
    }

    /**
     * This method converts a list of{@link User} (DB) to a List of {@link User} (Domain)
     * @param users {@link List<User>}
     * @return {@link List<UserDom>}
     */
    public List<UserDom> toDomain(List<User> users){
        List<UserDom> userDoms = new ArrayList<>();
        for (User user :users){
            userDoms.add(toDomain(user));
        }
        return userDoms;
    }

    /**
     * This method converts a {@link UserDom} (Domain) to a {@link User} (DB)
     * @param userDom {@link UserDom}
     * @return {@link User}
     */
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
}

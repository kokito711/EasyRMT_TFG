/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.RoleRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserDom findUser(String username){
        User user = userRepository.findByUsername(username);
        UserDom userDom = userConverter.toDomain(user);
        return userDom;
    }

    public void createUser(UserDom userDom){
        User user = userConverter.toModel(userDom);
        user.setPassword(bCryptPasswordEncoder.encode(userDom.getPassword()));
        userRepository.save(user);
    }
}

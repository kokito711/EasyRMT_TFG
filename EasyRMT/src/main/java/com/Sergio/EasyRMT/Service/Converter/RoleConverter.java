/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.RoleDom;
import com.Sergio.EasyRMT.Model.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleConverter {

    public Set<RoleDom> toDomain(Set<Role> roles){
        Set<RoleDom> roleDoms = new HashSet<>();
        for(Role role : roles){
            roleDoms.add(toDomain(role));
        }
        return roleDoms;
    }

    public RoleDom toDomain(Role role){
        RoleDom roleDom = new RoleDom(
                role.getRoleId(),
                role.getRole()
        );
        return roleDom;
    }

    public Role toModel(RoleDom roleDom){
        Role role = new Role();
        role.setRole(role.getRole());
        return role;
    }

    public Set<Role> toModel (Set<RoleDom> roleDoms){
        Set<Role> roles = new HashSet<>();
        for(RoleDom roleDom : roleDoms){
            roles.add(toModel(roleDom));
        }
        return roles;
    }
}

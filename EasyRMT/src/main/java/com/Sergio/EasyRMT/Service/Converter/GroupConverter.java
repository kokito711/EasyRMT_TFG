/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service.Converter;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupConverter {

    @Autowired
    public GroupConverter( ) {}

    public List<GroupDom> toDomain(List<Group> groups) {
        List<GroupDom> groupDomList = new ArrayList<>();
        for (Group group : groups){
            groupDomList.add(toDomain(group));
        }
        return groupDomList;
    }

    public GroupDom toDomain(Group group){
        GroupDom groupDom = new GroupDom(
                group.getGroup_id(),
                group.getName(),
                group.getGroup()
        );
        return groupDom;
    }
}

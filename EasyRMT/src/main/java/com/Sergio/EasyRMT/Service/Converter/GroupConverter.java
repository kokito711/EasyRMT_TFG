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

    /**
     * This method converts a list of {@link Group} (DB) into a list of {@link GroupDom} (Domain)
     * @param groups {@link List<Group>}
     * @return {@link List<GroupDom>}
     */
    public List<GroupDom> toDomain(List<Group> groups) {
        List<GroupDom> groupDomList = new ArrayList<>();
        for (Group group : groups){
            groupDomList.add(toDomain(group));
        }
        return groupDomList;
    }

    /**
     * This method converts a {@link Group} (DB) into a {@link GroupDom} (Domain)
     * @param group {@link Group}
     * @return {@link GroupDom}
     */
    public GroupDom toDomain(Group group){
        GroupDom groupDom = new GroupDom(
                group.getGroup_id(),
                group.getName(),
                group.getGroup()
        );
        return groupDom;
    }

    /**
     * This method converts a {@link GroupDom} (Domain) into a {@link Group} (DB)
     * @param groupDom {@link GroupDom}
     * @return {@link Group}
     */
    public Group toModel(GroupDom groupDom) {
        Group group = new Group();
        group.setName(groupDom.getName());
        return group;
    }
}

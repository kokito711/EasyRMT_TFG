/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Service;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Repository.GroupRepository;
import com.Sergio.EasyRMT.Service.Converter.GroupConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private UserService userService;
    private GroupRepository groupRepository;
    private GroupConverter groupConverter;

    @Autowired
    public GroupService(UserService userService, GroupRepository groupRepository, GroupConverter groupConverter) {
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
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
}

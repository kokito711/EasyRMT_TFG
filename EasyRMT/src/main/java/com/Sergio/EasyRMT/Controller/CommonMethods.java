/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonMethods {

    private ProjectService projectService;

    @Autowired
    public CommonMethods(ProjectService projectService) {
        this.projectService = projectService;
    }

    public boolean isPM(UserDom user, String name){
        for(Group_user groupDom : user.getGroups()) {
            if (groupDom.isPM() && groupDom.getPrimaryKey().getUser().getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<ProjectDom> getProjectsFromGroup(UserDom user){
        List<ProjectDom> projectDomList = new ArrayList<>();
        for(Group_user groupDom : user.getGroups()) {
            int groupId = groupDom.getPrimaryKey().getGroup().getGroup_id();
            List<ProjectDom> projectsFromGroup = projectService.getProjects(groupId);
            projectDomList.addAll(projectsFromGroup);
        }
        return projectDomList;
    }

    public boolean isAllowed(List<ProjectDom> projectDomList, ProjectDom projectDom){
        return projectDomList.contains(projectDom);
    }

    public boolean isStakeholder(UserDom user, String name, ProjectDom project) {
        for(Group_user groupDom : user.getGroups()){
            int  groupId = groupDom.getPrimaryKey().getGroup().getGroup_id();
            List<ProjectDom> projectDoms = projectService.getProjects(groupId);
            for(ProjectDom projectDom : projectDoms){
                if (projectDom.equals(project)){
                    if (groupDom.getPrimaryKey().getUser().getUsername().equals(name) && groupDom.isStakeholder()){
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}

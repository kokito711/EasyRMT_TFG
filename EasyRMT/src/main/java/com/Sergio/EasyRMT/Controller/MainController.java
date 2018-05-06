
/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Group;
import com.Sergio.EasyRMT.Model.Group_user;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    ProjectService projectService;
    UserService userService;

    @Autowired
    public MainController(ProjectService projectService, UserService userService)
    {
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(Principal principal){
        ModelAndView modelAndView = new ModelAndView("dashboard");

        modelAndView.addObject("user", principal.getName());
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = new ArrayList<>();
        boolean isPm = false;
        for(Group_user groupDom : user.getGroups()){
            if(groupDom.isPM() && groupDom.getPrimaryKey().getUser().getUsername().equals(principal.getName())){
                isPm = true;
            }
            int groupId = groupDom.getPrimaryKey().getGroup().getGroup_id();
            List<ProjectDom> projectsFromGroup = projectService.getProjects(groupId);
            projectDomList.addAll(projectsFromGroup);
        }
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("isPM", isPm);
        return modelAndView;
    }

}

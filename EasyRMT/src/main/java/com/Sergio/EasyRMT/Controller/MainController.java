
/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import com.Sergio.EasyRMT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
public class MainController {
    private ProjectService projectService;
    private UserService userService;
    private CommonMethods commonMethods;

    @Autowired
    public MainController(ProjectService projectService, UserService userService, CommonMethods commonMethods) {
        this.projectService = projectService;
        this.userService = userService;
        this.commonMethods = commonMethods;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(Principal principal){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", principal.getName());
        UserDom user = userService.findUser(principal.getName());
        List<ProjectDom> projectDomList = commonMethods.getProjectsFromGroup(user);
        boolean isPm = commonMethods.isPM(user,principal.getName());
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("isPM", isPm);
        return modelAndView;
    }

}

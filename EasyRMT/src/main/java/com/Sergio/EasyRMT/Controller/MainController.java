
/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.ProjectDom;

import com.Sergio.EasyRMT.Service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
public class MainController {
    ProjectService projectService;

    @Autowired
    public MainController(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(Principal principal){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("projectList", projectDomList);
        modelAndView.addObject("user", principal.getName());
        return modelAndView;
    }

}

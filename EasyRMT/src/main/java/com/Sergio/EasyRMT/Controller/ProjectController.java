package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView createProject(@ModelAttribute/* @Valid*/ ProjectDom project){
        ProjectDom projectDom = projectService.createProject(project);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", projectDom);
        return modelAndView;
    }
}

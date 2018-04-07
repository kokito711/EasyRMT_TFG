package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("projectList", projectDomList);
        return modelAndView;
    }

    @RequestMapping(value="/createProject", method = RequestMethod.GET)
    public ModelAndView createProject(ModelAndView model){
        model.setViewName("createProject");
        model.addObject("project",new ProjectDom());
        model.addObject("reqTypes", projectService.getReqTypes());
        return model;
    }

}

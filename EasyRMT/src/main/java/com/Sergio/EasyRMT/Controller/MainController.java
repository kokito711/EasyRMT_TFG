package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    ProjectService projectService;

    @GetMapping(value = "/dashboard")
    public String getDashboard(Model model){
        List<ProjectDom> projectDomList = projectService.getProjects();
        model.addAttribute("projectList", projectDomList);
        return "dashboard";
    }
}

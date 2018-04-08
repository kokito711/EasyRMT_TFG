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

    /**
     * This method gets the request of a project creation.
     * Then calls {@link ProjectService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link ProjectDom} as object.
     * @param project {@link ProjectDom} object returned from view to
     * @return ModelAndView with a project view and the persisted
     *       {@link ProjectDom} as object.
     */
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView createProject(@ModelAttribute @Valid ProjectDom project){
        ProjectDom projectDom = projectService.createProject(project);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", projectDom);
        return modelAndView;
    }


}

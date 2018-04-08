package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

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
        List<ProjectDom> projectDomList = projectService.getProjects();
        ProjectDom projectDom = projectService.createProject(project);
        ModelAndView modelAndView = new ModelAndView("project");
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        return modelAndView;
    }


    /**
     * This method gets the request of a project.
     * Then calls {@link ProjectService}to manage it.
     * When the information is returned this method generate a new ModelAndView with a project view and the persisted
     * {@link ProjectDom} as object.
     * @param id {@link ProjectDom} id to find it in database.
     * @return ModelAndView with a project view and the persisted
     *       {@link ProjectDom} as object.
     */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ModelAndView getProject(@PathVariable int id){
        List<ProjectDom> projectDomList = projectService.getProjects();
        ModelAndView modelAndView = new ModelAndView("project");
        ProjectDom projectDom = projectService.getProject(id);
        modelAndView.addObject("project", projectDom);
        modelAndView.addObject("projectList", projectDomList);
        return modelAndView;
    }


}

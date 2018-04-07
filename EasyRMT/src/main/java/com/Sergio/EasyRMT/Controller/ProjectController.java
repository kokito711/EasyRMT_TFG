package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.ProjectDom;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class ProjectController {

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public String createProject(@ModelAttribute @Valid ProjectDom project){
        return "";
    }
}

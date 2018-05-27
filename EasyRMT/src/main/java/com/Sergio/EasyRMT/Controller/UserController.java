/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;


import com.Sergio.EasyRMT.Domain.ProjectDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final String USER_BASE_PATH = "/user/";
    private UserService userService;
    private CommonMethods commonMethods;

    @Autowired
    public UserController(UserService userService, CommonMethods commonMethods) {
        this.userService = userService;
        this.commonMethods = commonMethods;
    }

    /**
     * This method retunrns the login view
     * @return login view
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect: /");
        return mav;
    }

    @RequestMapping(value = "/login_error", method = RequestMethod.GET)
    public ModelAndView login_error(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login_error");
        return mav;
    }

    /**
     * This method returns the user profile view with an user obtained from db
     * @param username user to be found in db
     * @return view with user
     */
    @RequestMapping(value = USER_BASE_PATH+"{username}", method = RequestMethod.GET)
    public ModelAndView getUserProfile(@PathVariable String username){
        UserDom user = userService.findUser(username);
        List<ProjectDom> projects = commonMethods.getProjectsFromGroup(user);
        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("userProf", user);
        modelAndView.addObject("user", user.getUsername());
        modelAndView.addObject("projectList", projects);
        return modelAndView;
    }

    /**
     * This method receives a request to update an user. then calls the userService to update user.
     * @param username username to be updated
     * @param userInfo user information to be updated
     * @return View with updated user
     */
    @RequestMapping(value = USER_BASE_PATH+"{username}", method = RequestMethod.POST)
    public ModelAndView updateUserProfile(@PathVariable String username,  @Valid UserDom userInfo, BindingResult result){
        UserDom user = userService.modifyUser(null,username, userInfo);
        List<ProjectDom> projects = commonMethods.getProjectsFromGroup(user);
        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("success", true);
        modelAndView.addObject("userProf", user);
        modelAndView.addObject("user", user.getUsername());
        modelAndView.addObject("projectList", projects);
        return modelAndView;
    }

}

/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;


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

@Controller
public class UserController {

    private final String USER_BASE_PATH = "/user/";
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @RequestMapping(value = USER_BASE_PATH+"{username}", method = RequestMethod.GET)
    public ModelAndView getUserProfile(@PathVariable String username){
        UserDom user = userService.findUser(username);
        ModelAndView modelAndView = new ModelAndView("/user/profile");
        modelAndView.addObject("user", user);
        return modelAndView;
    }


}

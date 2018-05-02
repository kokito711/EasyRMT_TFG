/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Locale;

@RestController
public class AdminController {
    private UserService userService;
    private final String BASE_PATH = "/admin/";
    private final String USER_BASE_PATH = BASE_PATH+"users";
    private final String GROUP_BASE_PATH = BASE_PATH+"groups";

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = BASE_PATH+"dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(){
        return new ModelAndView("/admin/dashboard");
    }

    @RequestMapping(value = USER_BASE_PATH+"/create", method = RequestMethod.GET)
    public ModelAndView createUser() {
        UserDom userDom = new UserDom();
        ModelAndView modelAndView = new ModelAndView("/admin/createUser");
        modelAndView.addObject("user", userDom);
        return modelAndView;
    }

    @RequestMapping(value = USER_BASE_PATH+"/create", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid UserDom user, BindingResult bindingResult, Locale locale) {
        ModelAndView modelAndView = new ModelAndView();
        UserDom userExists = userService.findUser(user.getUsername());
        if (userExists != null) {
            if(locale.getLanguage().equals("es_ES")){
                bindingResult
                        .rejectValue("username", "error.user",
                                "Ya existe un usuario con ese nombre");
            }
            else {
                bindingResult
                        .rejectValue("username", "error.user",
                                "There is already a user registered with the username provided");
            }
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/admin/createUser");
            for(FieldError error : bindingResult.getFieldErrors()){
                switch (error.getField()){
                    case "username":
                        user.setUsername("");
                        break;

                    case "email":
                        user.setEmail("");
                        break;
                    case "password":
                        user.setPassword("");
                        break;
                    case "name":
                        user.setName("");
                        break;
                    case "lastName":
                        user.setLastName("");
                        break;
                    case "phone":
                        user.setPhone("");
                        break;
                    default:
                        break;
                }
            }
            modelAndView.addObject("user",user);
            modelAndView.addObject("success", false);
        } else {
            userService.createUser(user);
            modelAndView.addObject("success", true);
            modelAndView.addObject("user", new UserDom());
            modelAndView.setViewName("/admin/createUser");
        }
        return modelAndView;
    }
}

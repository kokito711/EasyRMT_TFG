/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.Controller;

import com.Sergio.EasyRMT.Domain.GroupDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Service.GroupService;
import com.Sergio.EasyRMT.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
public class AdminController {
    private UserService userService;
    private GroupService groupService;
    private final String BASE_PATH = "/admin/";
    private final String USER_BASE_PATH = BASE_PATH+"users";
    private final String GROUP_BASE_PATH = BASE_PATH+"groups";

    @Autowired
    public AdminController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * This mehtod returns the view of admin dashboard
     * @return Model And View admin dashboard
     */
    @RequestMapping(value = BASE_PATH+"dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard(){
        return new ModelAndView("/admin/dashboard");
    }

    /**
     * This method returns the view of create user with an empty user
     * @return Model and view create user with empty user
     */
    @RequestMapping(value = USER_BASE_PATH+"/create", method = RequestMethod.GET)
    public ModelAndView createUser() {
        UserDom userDom = new UserDom();
        ModelAndView modelAndView = new ModelAndView("/admin/createUser");
        modelAndView.addObject("user", userDom);
        return modelAndView;
    }

    /**
     * This method receives a request to create a user. Then, check errors or call userService to create it
     * @param user User to be created
     * @param bindingResult possible errors
     * @param locale page language
     * @return user with empty fields when field has an error or page with empty user if user has been created.
     */
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
            modelAndView.addObject("user",user);
            modelAndView.addObject("success", false);
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

    /**
     * This method returns a view with all existing users
     * @return Model and view with all existing users
     */
    @RequestMapping(value = USER_BASE_PATH, method = RequestMethod.GET)
    public ModelAndView getUserList(){
        List<UserDom> users = userService.getUsers();
        ModelAndView modelAndView = new ModelAndView(USER_BASE_PATH);
        modelAndView.addObject("userList",users);
        return modelAndView;
    }

    /**
     * This method returns a profile from an user requested
     * @param userId id of user requested
     * @return View with user profile
     */
    @RequestMapping(value = USER_BASE_PATH+"/user/{userId}")
    public ModelAndView getUser(@PathVariable int userId){
        UserDom user = userService.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView("/admin/userProfile");
        modelAndView.addObject("userProf", user);
        return modelAndView;
    }
    /**
     * This method is a request to delete a user
     * @param userId id of user
     * @return status of deletion. 200 if ok and 500 if not ok
     */
    @RequestMapping(value = USER_BASE_PATH+"/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable int userId){
        boolean response = userService.deleteUser(userId);
        if(response){
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    /**
     * This method request a view of an specific user. Controller will call service to get user information and then
     * it will return this info.
     * @param userId user to get the info
     * @return Model And View with user information
     */
    @RequestMapping(value = USER_BASE_PATH+"/{userId}", method = RequestMethod.GET)
    public ModelAndView getEditUser(@PathVariable int userId){
        UserDom user = userService.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView("/admin/modifyUser");
        modelAndView.addObject("user",user);
        return modelAndView;
    }

    /**
     * This method calls user service to update user, and returns a view with user updated
     * @param userId id of user to be updated
     * @param user user with updated information
     * @return page with modified user if user has been updated.
     */
    @RequestMapping(value = USER_BASE_PATH+"/{userId}", method = RequestMethod.POST)
    public ModelAndView editUser(@PathVariable int userId, @Valid UserDom user, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/modifyUser");
        UserDom userDom = userService.modifyUser(userId,null,user);
        modelAndView.addObject("success", true);
        modelAndView.addObject("user", userDom);
        return modelAndView;
    }

    /**
     * This method returns a list with all existing groups in db
     * @return View with all existing groups
     */
    @RequestMapping(value = GROUP_BASE_PATH, method = RequestMethod.GET)
    public ModelAndView getGroupList(){
        ModelAndView modelAndView = new ModelAndView("/admin/groups");
        List<GroupDom> groups = groupService.findAll();
        modelAndView.addObject("groupList", groups);
        return modelAndView;
    }

    /**
     * This method receives a request to create a group. Method checks if PM is in group list. If it is method will
     * return error. If not, method will try to create a group
     * @return view with error if PM is in user list or view with success and new group if group has been created
     */
    @RequestMapping(value = GROUP_BASE_PATH+"/create", method = RequestMethod.POST)
    public ModelAndView createGroup(@Valid GroupDom group, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView("/admin/createGroup");
        List<UserDom> users = userService.getNoAdminUsers();
        modelAndView.addObject("userList", users);
        for(String userS: group.getStringUsers()){
            UserDom userDom = new UserDom();
            for(UserDom user : users){
                if(user.getUsername().equals(userS)){
                    userDom = user;
                    break;
                }
            }
            boolean wrongContext = false;
            if (userDom.getUserId() == group.getPm()){
                wrongContext = true;
            }
            else if(userDom.getUsername().equals(userS) && group.getStakeholders().contains(userS)){
                wrongContext = true;
            }
            if(wrongContext){
                modelAndView.addObject("group",group);
                modelAndView.addObject("success",false);
                return modelAndView;
            }
        }
        for(String userS: group.getStakeholders()){
            UserDom userDom = new UserDom();
            for(UserDom user : users){
                if(user.getUsername().equals(userS)){
                    userDom = user;
                    break;
                }
            }
            boolean wrongContext = false;
            if (userDom.getUserId() == group.getPm()){
                wrongContext = true;
            }
            else if(userDom.getUsername().equals(userS) && group.getStringUsers().contains(userS)){
                wrongContext = true;
            }
            if(wrongContext){
                modelAndView.addObject("group",group);
                modelAndView.addObject("success",false);
                return modelAndView;
            }
        }
        groupService.createGroup(group);
        modelAndView.addObject("group", new GroupDom());
        modelAndView.addObject("success",true);
        return modelAndView;
    }

    /**
     * This method receives a request to send the view with the form to create groups and provides it
     * @return view with create group page
     */
    @RequestMapping(value = GROUP_BASE_PATH+"/create", method = RequestMethod.GET)
    public ModelAndView getCreateGroup(){
        ModelAndView modelAndView = new ModelAndView("/admin/createGroup");
        List<UserDom> users = userService.getNoAdminUsers();
        modelAndView.addObject("userList", users);
        modelAndView.addObject("group", new GroupDom());
        return modelAndView;
    }
}

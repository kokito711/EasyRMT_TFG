package com.Sergio.EasyRMT.Controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {



    @GetMapping(value = "/dashboard")
    public String getDashboard(Model model){
        return "dashboard";
    }
}

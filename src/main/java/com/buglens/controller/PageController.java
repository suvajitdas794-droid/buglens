package com.buglens.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
    @GetMapping({"/","/dashboard","/analyzer","/history","/playground","/settings"})
    public String app(){return "index";}
}
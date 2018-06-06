package com.github.dianamaftei.yomimashou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }

    //Match everything without a suffix
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect() {
        // in order to handle routes properly
        return "index.html";
    }
}

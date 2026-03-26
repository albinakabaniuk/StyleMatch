package com.stylematch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward to index.html so React Router can handle the URL
        return "forward:/index.html";
    }
}

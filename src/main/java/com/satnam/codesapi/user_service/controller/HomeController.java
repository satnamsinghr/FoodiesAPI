package com.satnam.codesapi.user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // This forwards to index.html in src/main/resources/static/
        return "forward:/index.html";
    }

    @GetMapping("/secure")
    public String secure() {
        // You can still keep this secure endpoint, but make it return plain text
        return "You are viewing a secure page!";
    }
}

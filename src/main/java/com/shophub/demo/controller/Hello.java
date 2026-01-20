package com.shophub.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    
   
    
    @GetMapping("/")
    public String root() {
        return "Hello, World!";
    }
}

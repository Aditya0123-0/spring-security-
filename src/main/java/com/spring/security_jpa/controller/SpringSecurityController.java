package com.spring.security_jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringSecurityController {

    @GetMapping
    public  String sayHello(){
        return "Hello Spring Security!";
    }

    @GetMapping("/client")
    public  String sayHelloClient(){
        return "Hello Spring Client!";
    }

    @GetMapping("/admin")
    public String sayHelloAdmin(){
        return "Hello Spring Admin!";
    }
}

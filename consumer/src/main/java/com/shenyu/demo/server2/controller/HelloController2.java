package com.shenyu.demo.server2.controller;

import com.shenyu.demo.server2.CallerService;
import com.shenyu.demo.server2.ModelAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;


@RestController
public class HelloController2 {

    @Autowired
    CallerService callerService;

    @GetMapping("/hello2")
    public String hello() throws URISyntaxException {
        return callerService.requestConnect("http://server1/hello", "123");
    }

}

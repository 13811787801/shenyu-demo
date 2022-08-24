package com.shenyu.demo.server2.controller;

import com.shenyu.demo.server2.ModelAnnotation;
import com.shenyu.demo.server2.feignClient.OldFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;


@RestController
public class HelloController1 {

    @Autowired
    OldFeignClient oldFeignClient;

    @GetMapping("/hello1")
    public String hello1() throws URISyntaxException {
        return oldFeignClient.requestConnect("aa");
    }
}

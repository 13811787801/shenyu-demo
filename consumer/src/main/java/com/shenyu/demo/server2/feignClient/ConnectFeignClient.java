package com.shenyu.demo.server2.feignClient;

import feign.RequestLine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;


//@FeignClient("server1")
public interface ConnectFeignClient {

    @PostMapping
    String requestConnect(URI url, String aa);

//    @GetMapping("/hello")
//    String requestConnect();

}

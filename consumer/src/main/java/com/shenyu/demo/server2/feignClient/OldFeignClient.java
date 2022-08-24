package com.shenyu.demo.server2.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("server1")
public interface OldFeignClient {

    @GetMapping("/hello")
    String requestConnect(String bb);
}

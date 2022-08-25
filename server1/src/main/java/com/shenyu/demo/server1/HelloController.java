package com.shenyu.demo.server1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello(HttpServletResponse response) throws UnknownHostException {
        log.info("ip:{}", InetAddress.getLocalHost().getHostAddress());
        return "Server1 Hello ShenYu";
    }
}

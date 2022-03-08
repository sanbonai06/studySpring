package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/hello")
    public String hello() {
        return "<h1>Hello Spring!</h1>";
    }
}

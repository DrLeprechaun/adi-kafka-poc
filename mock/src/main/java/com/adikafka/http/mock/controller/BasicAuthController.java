package com.adikafka.http.mock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class BasicAuthController {

    @GetMapping(path="/basic")
    public String basicAuth()
    {
        return "Basic authentication OK";
    }
}

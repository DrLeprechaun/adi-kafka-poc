package com.adikafka.http.mock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/logout")
public class LogoutController {

    @GetMapping(path="/ok")
    public String basicLogot()
    {
        return "Logout OK";
    }
}

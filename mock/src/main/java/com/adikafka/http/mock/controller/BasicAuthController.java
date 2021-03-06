package com.adikafka.http.mock.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/auth")
public class BasicAuthController {

    @GetMapping(path="/basic")
    public String basicAuthGet()
    {
        return "Basic authentication OK";
    }

    @PostMapping (path="/basic", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String basicAuthPost(HttpEntity<String> httpEntity)
    {
        String json = httpEntity.getBody();
        System.out.println(json);
        return "Basic authentication OK";
    }
}

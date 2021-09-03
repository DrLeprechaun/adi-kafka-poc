package com.adikafka.http.mock.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/auth")
public class KeyAuthSecretController {

    @GetMapping(path="/keysecret")
    public String keyAuthGet()
    {
        return "KeyAuth Secret authentication OK";
    }

    @PostMapping(path="/keysecret", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String keyAuthPost(HttpEntity<String> httpEntity)
    {
        String json = httpEntity.getBody();
        System.out.println(json);
        return "KeyAuth Secret authentication OK";
    }
}

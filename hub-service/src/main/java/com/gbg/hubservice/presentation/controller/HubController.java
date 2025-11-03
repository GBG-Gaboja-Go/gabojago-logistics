package com.gbg.hubservice.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hubs")
public class HubController {

    @GetMapping
    public String test() {

        return "Hub Controller Test!";
    }
}
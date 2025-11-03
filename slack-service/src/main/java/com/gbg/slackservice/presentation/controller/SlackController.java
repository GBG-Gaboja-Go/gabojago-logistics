package com.gbg.slackservice.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/slacks")
public class SlackController {

    @GetMapping
    public String test() {

        return "Slack Controller Test!";
    }
}
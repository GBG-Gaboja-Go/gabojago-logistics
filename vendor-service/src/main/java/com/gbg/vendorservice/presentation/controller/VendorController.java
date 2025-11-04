package com.gbg.vendorservice.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vendors")
public class VendorController {

    @GetMapping
    public String test() {

        return "Vendor Controller Test!";

    }

}
package com.epam.addressbook.restsupport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringDefaultController {
    @GetMapping("/")
    public String defaultRoute() {
        return "Hello!!!";
    }
}
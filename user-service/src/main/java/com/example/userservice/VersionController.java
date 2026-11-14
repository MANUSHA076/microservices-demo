package com.example.userservice;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of("version", "v2", "deployedBy", "github-actions");
    }
}

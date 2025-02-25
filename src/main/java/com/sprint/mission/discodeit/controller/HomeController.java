package com.sprint.mission.discodeit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping()
    public ResponseEntity home() {
        return ResponseEntity.ok(200);
    }
}

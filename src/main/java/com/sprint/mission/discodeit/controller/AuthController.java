package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/login")
public class AuthController {

    @RequestMapping(method = RequestMethod.POST)
    public String login() {

    }
}

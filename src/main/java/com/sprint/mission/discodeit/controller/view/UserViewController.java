package com.sprint.mission.discodeit.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {

    @GetMapping
    public String findAll() {
        return "user-list";
    }
}

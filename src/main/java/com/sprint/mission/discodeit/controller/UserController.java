package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userView")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String userPage(){
        return "userList";
    }
}

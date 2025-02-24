package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    @GetMapping("/userView")
    public String getUserList(Model model) {
        List<UserDTO.response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list"; // user-list.html 렌더링
    }
}


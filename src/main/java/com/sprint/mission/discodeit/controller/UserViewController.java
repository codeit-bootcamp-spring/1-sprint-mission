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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;

    // 메인 페이지 렌더링
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html 렌더링
    }

    @GetMapping("/list")
    public String getUserList(Model model) {
        List<UserDTO.response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list"; // user-list.html 렌더링
    }
}


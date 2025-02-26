package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("profileImage");
    }

    // 유저 목록 조회
    @GetMapping
    public String userList(Model model) {
        List<UsersDTO> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UsersDTO());
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UsersDTO usersDTO,
                               @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        byte[] imageBytes = null;
        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                imageBytes = profileImage.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        User createdUser = userService.create(usersDTO, imageBytes);
        usersDTO.setId(createdUser.getId());
        return "redirect:/users";
    }

    // 유저 삭제
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @PatchMapping("/{id}/online")
    public ResponseEntity<Void> updateOnlineStatus(
            @PathVariable String id,
            @RequestParam boolean status
    ) {
        userService.updateOnlineStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
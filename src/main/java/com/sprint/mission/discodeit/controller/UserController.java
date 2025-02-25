package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("profileImage");
    }

    @GetMapping
    public String userList(Model model) {
        List<UsersDTO> users = userService.findAll();

        if (users == null) {
            users = new ArrayList<>();
            log.warn("경고: userService.findAll()이 null을 반환했습니다.");
        }

        log.info("UserController.userList() 호출됨. 결과 크기: " + users.size());

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
    public String registerUser(@ModelAttribute User user, @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setOnline(false);

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                byte[] imageBytes = profileImage.getBytes();
                user.setProfileImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//             user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);

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
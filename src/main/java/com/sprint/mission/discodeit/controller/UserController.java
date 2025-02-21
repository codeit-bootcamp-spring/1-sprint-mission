package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    public UserController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    // 1. 사용자 등록 (POST)
    @PostMapping("/user")
    public User createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request, Optional.empty());
    }

    // 2. 사용자 정보 수정 (PUT)
    @PutMapping( "/user/{userId}")
    public User updateUser(@PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
        return userService.update(userId, request, Optional.empty());
    }

    // 3. 사용자 삭제 (DELETE)
    @DeleteMapping( "/user/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }

    // 4. 모든 사용자 조회 (GET)
    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    // 5. 특정 사용자 조회 (GET)
    @GetMapping("/user/{userId}")
    public UserDto getUser(@PathVariable UUID userId) {
        return userService.find(userId);
    }

    @GetMapping("/info")
    public String getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if(user != null) {
            return "로그인된 사용자: " + user.getUsername();
        } else {
            return "로그인되지 않은 사용자입니다.";
        }
    }


}

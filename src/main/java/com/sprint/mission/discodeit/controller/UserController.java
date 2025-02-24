package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.authService.LoginRequest;
import com.sprint.mission.discodeit.dto.authService.LoginResponse;
import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.dto.userService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.dto.userService.UserProfileImageRequest;
import com.sprint.mission.discodeit.dto.userService.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.auth.AuthService;
import com.sprint.mission.discodeit.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;



    // 사용자 생성 API (POST /users/create)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User createUser(@RequestBody UserCreateRequest userRequest,
                           @RequestBody(required = false) BinaryContentCreateRequestDTO profileImage) {
        return userService.create(userRequest, Optional.ofNullable(profileImage));
    }


    //  사용자 정보 수정 API (PUT /users/update)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public User updateUser(@RequestBody UserUpdateRequest userRequest) {
        return userService.update(userRequest, Optional.empty());
    }



    //  특정 사용자 조회 API (GET /users/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDTO getUser(@PathVariable UUID id) {
        return userService.find(id);
    }

    // 사용자 삭제 API (DELETE /users/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    //  사용자 조회 API (GET /users)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    //  로그인 API (POST /users/login)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

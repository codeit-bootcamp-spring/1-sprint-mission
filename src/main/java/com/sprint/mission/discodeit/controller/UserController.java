package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    // 단일 사용자 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDto getUser(@PathVariable("id") UUID id) {
        return userService.findUserById(id);
    }

    // 전체 사용자 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    // 사용자 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserDto updateUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    // 사용자 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }

    // 로그인
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AuthDto.LoginResponse login(@RequestBody AuthDto.LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}

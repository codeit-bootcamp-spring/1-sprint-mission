package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //사용자 단일 조회
    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable String userId) {
        return userService.findById(userId);
    }

    //todo - 사용자 등록
    @PostMapping
    public UserResponseDto createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.create(createUserDto);
    }

    //todo - 사용자 정보 수정
    @PutMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable String userId, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(userId, updateUserDto);
    }

    //todo - 사용자 삭제
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        if(userService.deleteUser(userId)){
            return "User deleted successfully";
        }
        return "Delete user failed";
    }

    // todo - 모든 사용자 조회
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userService.findAll();
    }

}

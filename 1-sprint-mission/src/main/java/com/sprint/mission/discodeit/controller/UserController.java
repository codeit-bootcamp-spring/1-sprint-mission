package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.*;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.dto.response.user.UserLoginResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.AuthService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    //사용자 등록
    @PostMapping
    public UserDTO createUser(@RequestBody UserCreateRequestDTO createRequestDTO) {
         return userService.create(createRequestDTO.userCreateDTO(),
                 createRequestDTO.userProfileImageDTO());
    }

    //사용자 정보 수정
    @PatchMapping
    public UserDTO updateUser(@RequestBody UserUpdateRequestDTO updateRequestDTO) {
        return userService.update(updateRequestDTO.userUpdateDTO()
                ,updateRequestDTO.userProfileImageDTO());
    }

    //사용자 삭제
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    //모든 사용자 조회
    @GetMapping("/list")
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    //사용자의 온라인 상태 업데이트
    @PatchMapping("/status")
    public UserLoginResponseDTO updateUserLoginStatus(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        return authService.login(userLoginRequestDTO);
    }

    //오류 테스트 앤드 포인트
    @GetMapping("/test-exception")
    public void testException() {
        throw new RuntimeException("Test error log");
    }



}

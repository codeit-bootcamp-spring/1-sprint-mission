package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.request.user.*;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PatchMapping("/status/update/{userId}")
    public UserStatusResponseDTO updateUserStatus(@PathVariable UUID userId) {
        return userStatusService.updateByUserId(userId);
    }



}

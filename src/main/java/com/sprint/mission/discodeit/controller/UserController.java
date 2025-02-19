package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/users")
    public ResponseDTO<UUID> joinUser(@ModelAttribute UserCreateDTO requset){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("사용자 등록 완료")
                .data(userService.create(requset))
                .build();
    }

    @GetMapping("/users")
    public ResponseDTO<List<UserFindDTO>> getAllUsers(){
        return ResponseDTO.<List<UserFindDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("모든 사용자 조회")
                .data(userService.findAll())
                .build();
    }

    @PutMapping("/users/{userId}")
    public ResponseDTO updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO request){
        userService.update(userId, request);
        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message("사용자 수정 완료")
                .build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseDTO<UUID> deleteUser(@PathVariable UUID userId){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("사용자 삭제 완료")
                .data(userService.delete(userId))
                .build();
    }

    @PutMapping("/users/{userId}/status")
    public ResponseDTO updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateDTO request){
        userStatusService.update(userId, request);
        return ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message("사용자 온라인 상태 업데이트 완료")
                .build();
    }

}

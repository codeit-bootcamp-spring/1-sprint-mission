package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.wrapper.UserCreateRequestWrapper;
import com.sprint.mission.discodeit.dto.wrapper.UserUpdateRequestWrapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequestWrapper userCreateRequestWrapper) {
        UserCreateRequest userCreateRequest = userCreateRequestWrapper.userCreateRequest();
        BinaryContentCreateRequest profileRequest = userCreateRequestWrapper.ProfileCreateRequest();

        UserDto userDto = userService.create(userCreateRequest, Optional.ofNullable(profileRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    // 사용자 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
                                              @RequestBody UserUpdateRequestWrapper userUpdateRequestWrapper) {
        UserUpdateRequest userUpdateRequest = userUpdateRequestWrapper.userUpdateRequest();
        BinaryContentCreateRequest profileRequest = userUpdateRequestWrapper.profileRequest();

        UserDto userDto = userService.update(userId, userUpdateRequest, Optional.ofNullable(profileRequest));
        return ResponseEntity.ok(userDto);
    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<UserDto>> searchAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    // 사용자 온라인 상태 업데이트
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserStatusDto> updateUserStatus(@PathVariable UUID userId,
                                                          @RequestBody UserStatusUpdateRequest userStatusUpdateRequest){
        UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
        return ResponseEntity.ok(userStatusDto);
    }
}

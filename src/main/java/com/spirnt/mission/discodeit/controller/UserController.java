package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestParam(required = true) String name,
                                        @RequestParam(required = true) String email,
                                        @RequestParam(required = true) String password,
                                        @RequestParam(required = false) MultipartFile profileImage) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(name, email, password, profileImage);
        User user = userService.create(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserCreateResponse(user.getId(), name, email, user.getProfileImageId()));
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable UUID userId,
                                        @RequestParam String name,
                                        @RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam(required = false) MultipartFile profileImage) {
        // 모든 필드가 null인지 확인
        if (name == null && email == null && password == null && profileImage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("You should enter at least one element to update."));
        }
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(name, email, password, profileImage);
        userService.update(userId, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 사용자 삭제
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // 모든 사용자 조회
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 사용자의 온라인 상태 업데이트
    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserStatus(@PathVariable UUID userId,
                                              @RequestBody UserStatusUpdate userStatusUpdate) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdate, Instant.now());
        UserStatusResponse userStatusResponse = new UserStatusResponse(userStatus.getUserId(),
                userStatus.getUserStatusType(), userStatus.getLastSeenAt());
        return ResponseEntity.ok(userStatusResponse);
    }

}


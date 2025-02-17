package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.response.ResultCode;
import com.sprint.mission.discodeit.global.response.ResultResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("")
    public ResponseEntity<ResultResponse<User>> createUser(
            @RequestPart(value = "createUserRequest") CreateUserRequest createUserRequest,
            @RequestPart(value = "profileImage") MultipartFile profileImage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResultResponse.of(ResultCode.USER_CREATED,
                        userService.createUser(createUserRequest, profileImage)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResultResponse<User>> updateUser(
            @PathVariable UUID id,
            @RequestPart(value = "updateUserRequest") UpdateUserRequest updateUserRequest,
            @RequestPart(value = "profileImage") MultipartFile profileImage) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultResponse.of(ResultCode.USER_UPDATED,
                        userService.updateUser(id, updateUserRequest, profileImage)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<UUID>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ResultResponse.of(ResultCode.USER_DELETED, id));
    }

    @GetMapping("")
    public ResponseEntity<ResultResponse<List<FindUserResponse>>> findAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultResponse.of(ResultCode.USER_LIST_FETCHED,
                        userService.findAllUsers()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResultResponse<Boolean>> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultResponse.of(ResultCode.USER_ONLINE_STATUS_UPDATED,
                        userStatusService.updateUserStatusByUserId(
                                id, updateUserStatusByUserIdRequest).getIsOnline()));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@ResponseBody
@Controller
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(
            path = "create",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<User> create(
            @ModelAttribute UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
        if (profile != null) {
            profileRequest = resolveProfileRequest(profile);
        }

        User createdUser = userService.create(userCreateRequest, profileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @RequestMapping(
            path = "update",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<User> update(
            @RequestParam("userId") UUID userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest,
            @RequestPart(value="profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
        if (profile != null) {
            profileRequest = resolveProfileRequest(profile); // TODO : 받은 프로필 '파일'을 b.c.c.Request Dto로 변환하는 작업?
        }

        User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("userId") UUID userId){
        userService.delete(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping(path = "findAll")
    public ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> allUsers = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allUsers);
    }

    @RequestMapping(path = "updateUserStatusByUserId")
    public ResponseEntity<Void> updateUserStatusByUserId(
            @RequestParam("userId") UUID userId,
            @ModelAttribute UserStatusUpdateRequest userStatusUpdateRequest
    ){
        userStatusService.updateByUserId(userId, userStatusUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

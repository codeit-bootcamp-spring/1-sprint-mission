package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;


import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> createUser(
            @RequestPart("userCreateRequest") String userCreateRequestStr,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserCreateRequest userCreateRequest = objectMapper.readValue(userCreateRequestStr, UserCreateRequest.class);

            BinaryContentCreateRequest imageRequest = null;
            if (profile != null && !profile.isEmpty()) {
                imageRequest = new BinaryContentCreateRequest(
                        profile.getOriginalFilename(),
                        profile.getContentType(),
                        profile.getBytes()
                );
            }

            UserResponse createdUser = userService.createUserWithProfileImage(userCreateRequest, imageRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (IOException e) {
            throw new IllegalArgumentException("프로필 이미지 처리 중 오류 발생: " + e.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUserById(@RequestParam("userid") UUID userId) {
        UserResponse user = userService.getUserById(userId);
        if (user == null) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<UserResponse> updateUser(
            @RequestParam("userid") UUID userId,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        UserResponse updatedUser = userService.updateUser(userId, userUpdateRequest);
        if (updatedUser == null) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUserWithProfileImage(
            @RequestParam("userid") UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        BinaryContentCreateRequest imageRequest = null;
        if (profile != null && !profile.isEmpty()) {
            try {
                imageRequest = new BinaryContentCreateRequest(
                        profile.getOriginalFilename(),
                        profile.getContentType(),
                        profile.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        UserResponse updatedUser = userService.updateUserWithProfileImage(userId, userUpdateRequest, imageRequest);
        if (updatedUser == null) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@RequestParam("userid") UUID userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
    }
}
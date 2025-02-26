package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTO> createUser(
            @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);
        User user = userService.create(userCreateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(user));
    }

    @RequestMapping(value = "/update/{userId}", method = RequestMethod.PUT, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);
        User user = userService.update(userId, userUpdateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(user));
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @RequestMapping(value = "/status/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request)
    {
        userStatusService.update(userId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BinaryContentCreateRequest(
                    profileFile.getOriginalFilename(),
                    profileFile.getContentType(),
                    profileFile.getBytes()
            ));
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                false
        );
    }

}

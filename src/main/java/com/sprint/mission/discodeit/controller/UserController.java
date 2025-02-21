package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserAPI;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
public class UserController implements UserAPI {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> createUser(@RequestPart UserCreate userCreate, @RequestPart(value = "profile", required = false)MultipartFile multipartFile) {
       Optional<BinaryContentCreateDto> profileRequest = Optional.ofNullable(multipartFile).flatMap(this::resolveProfileRequest);
       User createUser = userService.createUser(userCreate, profileRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        UserDto userDto = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PatchMapping(value = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UserUpdate userUpdate, @RequestBody(required = false) MultipartFile profile) {
        Optional<BinaryContentCreateDto> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);
        User updatedUser = userService.update(userId, userUpdate, profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //userStatus
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdate userStatusUpdate) {
        UserStatus updateStatus = userStatusService.updateByUserId(userId, userStatusUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updateStatus);
    }

    private Optional<BinaryContentCreateDto> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

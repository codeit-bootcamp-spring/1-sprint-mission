package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public BinaryContentCreateRequest resolveProfileRequest(MultipartFile profile) {
        try {
            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            );
            return binaryContentCreateRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.readAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @RequestMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> create(@RequestPart UserCreateRequest userCreateRequest, @RequestPart(required = false) MultipartFile profile) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .map(this::resolveProfileRequest);

        User createdUser = userService.createUser(userCreateRequest, profileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    } //param으로 optional을 쓰면X -> null check(isPresent)가 필요해서? -> ofNullable + 함수형느낌(by .map)으로 작성

    @RequestMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> update(@RequestParam("userId") UUID userId, @RequestPart("userUpdateRequest")UserUpdateRequest userUpdateRequest, @RequestPart (value = "profile", required = false) MultipartFile profile) {
//        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
//        if (profile != null) {
//            profileRequest = resolveProfileRequest(profile);
//        } 전형적인 optional 장점 못살리는 코드. (이렇게 하려면 resolveProfileRequest 반환형 Optional<T>로 변화 필요)
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .map(this::resolveProfileRequest);

        User updatedUser = userService.updateUserField(userId, userUpdateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @RequestMapping(value = "/delete")
    public ResponseEntity<Void> delete(@RequestParam("userId") UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}



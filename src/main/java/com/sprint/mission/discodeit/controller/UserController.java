package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 목록 조회", description = "전체 회원 조회")
    @GetMapping
    public ResponseEntity<List<UsersDto>> listUsers() {
        List<UsersDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원 목록 조회", description = "단일 회원 조회")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        UserDto user = userService.find(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "회원 가입", description = "회원 가입")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsersDto> registerUser(@Valid
            @RequestPart("user") UsersDto usersDTO,
            @RequestPart(value = "pro", required = false) MultipartFile pro) {

        byte[] profileImage = null;
        try {
            if (pro != null && !pro.isEmpty()) {

                profileImage = pro.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        UsersDto users = userService.create(usersDTO, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsersDto> updateUser(
            @PathVariable String id,
            @Valid @RequestPart("user") UsersDto usersDto,
            @RequestPart(value = "pro", required = false) MultipartFile pro) {

        byte[] profileImage = null;
        try {
            if (pro != null && !pro.isEmpty()) {
                profileImage = pro.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        UsersDto updateUser = userService.update(id, usersDto, profileImage);

        if (updateUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateUser);
    }


    @Operation(summary = "유저 삭제", description = "회원 정보 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상태 업데이트", description = "사용자의 온라인 상태 업데이트")
    @PatchMapping("/{id}/online-status")
    public ResponseEntity<Void> updateOnlineStatus(
            @PathVariable String id,
            @RequestParam boolean status) {

        userService.updateOnlineStatus(id, status);
        return ResponseEntity.ok().build();
    }

}
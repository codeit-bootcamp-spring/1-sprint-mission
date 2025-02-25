package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // 회원 목록
    @GetMapping
    public ResponseEntity<List<UsersDto>> listUsers() {
        List<UsersDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 회원 가입
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsersDto> registerUser(@Valid
            @RequestPart("user") UsersDto usersDTO,
            @RequestPart(value = "pof", required = false) MultipartFile pof) {

        byte[] profileImage = null;
        try {
            if (pof != null && !pof.isEmpty()) {
                profileImage = pof.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        UsersDto users = userService.create(usersDTO, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    // 유저 변경
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> modifedUser(@Valid @PathVariable String id) {
//        userService.find(id);
//    }


    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
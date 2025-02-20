package com.sprint.mission.discodeit.controller;

import ch.qos.logback.core.boolex.EvaluationException;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor()
public class UserController {
    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.create(userDTO, null);
        return ResponseEntity.ok(user);
    }

    //프로필 이미지 포함 회원 가입
    @PostMapping("/upload")
    public ResponseEntity<User> createUserWithProfile(
            @RequestPart("user") UserDTO userDTO,
            @RequestPart("profileImage") byte[] profileImage) {
        User user = userService.create(userDTO, profileImage);
        return ResponseEntity.ok(user);
    }

    //유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.find(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    //유저 수정
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO,
            @RequestPart(value = "profileImage", required = false) byte[] profileImage) {
        User updateUser = userService.update(id, userDTO, profileImage);
        return ResponseEntity.ok(updateUser);
    }

    //유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

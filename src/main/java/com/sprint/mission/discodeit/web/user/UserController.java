package com.sprint.mission.discodeit.web.user;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<List<CheckUserDto>> create() {
        BinaryContent profile= new BinaryContent("profile.png",100L,"image/png","this is test".getBytes(StandardCharsets.UTF_8));
        User alice = new User("alice","alice123","Alice", "alice@example.code",profile);
        User hyun = new User("hyun","hyun123","Hyun", "hyun@example.code");
        User yull = new User("yull","yull123","Yull", "yull@example.code");

        userService.createUser(alice);
        userService.createUser(hyun);
        userService.createUser(yull);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.findAllUsers());
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<Optional<CheckUserDto>> findUserByLoginId(@PathVariable String loginId) {
        Optional<CheckUserDto> byloginId = userService.findByloginId(loginId);
        return ResponseEntity.status(HttpStatus.OK).body(byloginId);
    }

    @GetMapping
    public ResponseEntity<List<CheckUserDto>> findAllUser() {
        List<CheckUserDto> allUsers = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<CheckUserDto>> update(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto updateUserDto) {
        userService.updateUser(id, updateUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<CheckUserDto>> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateOnline(@PathVariable UUID id) {
        userService.updateuserStatus(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

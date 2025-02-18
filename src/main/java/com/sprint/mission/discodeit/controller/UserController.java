package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserBaseDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdate;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping
    public ResponseEntity<UserBaseDTO> createUser(@RequestBody UserCreateDTO createDTO) {
        UserBaseDTO userBase = userService.createUser(createDTO);
        return ResponseEntity.ok(userBase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO updateDTO){
        return ResponseEntity.ok(userService.update(id,updateDTO));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID id, @RequestBody UserStatusUpdate statusUpdateDTO) {
        UserStatus updatedUserStatus = userStatusService.update(id, statusUpdateDTO);
        return ResponseEntity.ok(updatedUserStatus);
    }
}

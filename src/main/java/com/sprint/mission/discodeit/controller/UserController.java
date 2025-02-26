package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String showUserList(Model model) {
        List<UserResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestPart UserCreateRequest userCreateRequest,
                                       @RequestPart(required = false) MultipartFile multipartFile) {
        Optional<BinaryContentRequest> binaryContentRequest = Optional.ofNullable(multipartFile)
                .flatMap(this::resolveProfileRequest);
        return ResponseEntity.ok(userService.create(userCreateRequest, binaryContentRequest));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody UserUpdateRequest userUpdateRequest,
                                       @RequestBody(required = false) BinaryContentRequest binaryContentRequest) {
        return ResponseEntity.ok(userService.update(id, userUpdateRequest, Optional.ofNullable(binaryContentRequest)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Optional<BinaryContentRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentRequest binaryContentCreateRequest = new BinaryContentRequest(
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

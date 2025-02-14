package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(
            @RequestBody UserCreateRequest userCreateRequest,
            @RequestBody(required = false) BinaryContentCreateRequest binaryContentCreateRequest
    ) {
        userService.create(userCreateRequest, Optional.ofNullable(binaryContentCreateRequest));
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUser(
            @PathVariable(name = "userId") UUID userId,
            @RequestBody UserUpdateRequest userUpdateRequest,
            @RequestBody(required = false) BinaryContentCreateRequest binaryContentCreateRequest
    ) {
        userService.update(userId, userUpdateRequest, Optional.ofNullable(binaryContentCreateRequest));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "userId") UUID userId
    ) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }
}

package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
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

    // 생성
    @RequestMapping(value = "", method = RequestMethod.POST)
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

    // 업데이트

    // 삭제
}

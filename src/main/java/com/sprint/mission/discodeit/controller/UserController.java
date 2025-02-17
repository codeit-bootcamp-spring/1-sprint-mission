package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequestWrapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public ResponseEntity<UserDto> CreateUser(@RequestBody UserCreateRequestWrapper userCreateRequestWrapper){
        UserCreateRequest userCreateRequest = userCreateRequestWrapper.userCreateRequest();
        Optional<BinaryContentCreateRequest> profileCreateRequest = userCreateRequestWrapper.profileCreateRequest();

        User createUser = userService.create(userCreateRequest, profileCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDto(
                createUser.getId(),
                createUser.getCreatedAt(),
                createUser.getUpdatedAt(),
                createUser.getUsername(),
                createUser.getEmail(),
                createUser.getProfileId(),
                null
        ));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.FileConverter;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FileConverter fileConverter;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<UserCreateResponse> create(
            @RequestPart("user") UserCreateRequest request,
            @RequestPart(value = "profile",
                    required = false) MultipartFile profile
    ) throws IOException {
        User user = userService.create(request, fileConverter.convertToBinaryRequest(profile));
        UserCreateResponse createResponse = UserCreateResponse.from(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createResponse);
    }

    @RequestMapping(value="/{userId}",method = RequestMethod.PUT, consumes = "multipart/form-data")
    public ResponseEntity<UserUpdateResponse> update(
            @PathVariable UUID userId,
            @RequestPart("user") UserUpdateRequest request,
            @RequestPart(value = "profile",
            required = false) MultipartFile profile
    ) throws IOException{
        User user = userService.update(userId, request, fileConverter.convertToBinaryRequest(profile));
        UserUpdateResponse updateResponse = UserUpdateResponse.from(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateResponse);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> find(
           @PathVariable UUID userId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.find(userId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}

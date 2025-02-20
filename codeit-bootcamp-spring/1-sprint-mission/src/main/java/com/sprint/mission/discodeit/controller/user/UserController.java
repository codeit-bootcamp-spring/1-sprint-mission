package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> createUser(
            @RequestPart(name = "user-create-request") UserCreateRequest userCreateRequest,
            @RequestPart(name = "binary-content-create-request", required = false) MultipartFile binaryContentCreateRequest
    ) {
        BinaryContentCreateRequest binaryContent = null;
        if (Objects.nonNull(binaryContentCreateRequest)) {
            try {
                binaryContent = new BinaryContentCreateRequest(
                        binaryContentCreateRequest.getOriginalFilename(),
                        binaryContentCreateRequest.getContentType(),
                        binaryContentCreateRequest.getBytes());
            } catch (IOException exception) {
                throw new IllegalArgumentException(exception);
            }
        }
        userService.create(userCreateRequest, Optional.ofNullable(binaryContent));
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

package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.UserApiDocs;
import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApiDocs {

    private final UserService userService;

    @GetMapping
    @Override
    public ResponseEntity<List<UserResponse>> getAllUser() {
        log.info("GET /api/users - getAllUsers");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity<UserResponse> createUser(
        @Valid @RequestPart("userCreateRequest") UserRequest.Create userRequest,
        @RequestPart(value = "profile", required = false) MultipartFile userProfileImage
    ) {
        log.info("POST /api/users - user: {}", userRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createUser(userRequest, userProfileImage));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable UUID userId,
        @Valid @RequestPart("userUpdateRequest") UserRequest.Update userRequest,
        @RequestPart(value = "profile", required = false) MultipartFile userProfileImage
    ) {

        log.info("PUT /api/users/{}", userId);
        return ResponseEntity.ok(userService.update(userId, userRequest, userProfileImage));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @DeleteMapping("/{userId}")
    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);

        log.info("DELETE /api/users/{}", userId);
        return ResponseEntity.noContent().build();
    }

}

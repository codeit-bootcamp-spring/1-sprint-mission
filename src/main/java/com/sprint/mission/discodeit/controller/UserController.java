package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<UserDto> create(
            @RequestPart("user") UserCreateRequest request,
            @RequestPart(value = "profile",
                    required = false) MultipartFile profile
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request, convertToBinaryRequest(profile)));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.find(userId));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = "multipart/form-data")
    public ResponseEntity<UserDto> update(
            @PathVariable UUID userId,
            @RequestPart("user") UserUpdateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        return ResponseEntity.ok(userService.update(
                userId
                , request, convertToBinaryRequest(profile)
        ));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    private Optional<BinaryContentCreateRequest> convertToBinaryRequest(MultipartFile file) throws IOException{

            if (file == null || file.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            ));
    }


}

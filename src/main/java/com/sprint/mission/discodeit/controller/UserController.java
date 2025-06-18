package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

/// / TODO: Mapping시 /api/v1/users 처럼 버전 추가 (확장성을 위함) / -> 프론트 코드,,, 내가 고치고 싶지 않아서 일단 지금은 냅두는 것

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserService userService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<UserResponseDto> createUser(
      @Valid @RequestPart("userCreateRequest") UserSignupRequestDto reqUserDto,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(reqUserDto, profile));
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.find(userId));
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
  @PatchMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable UUID userId,
      @RequestPart(value = "userUpdateRequest", required = false) UserUpdateDto userUpdateDto,
      @RequestPart(value = "profile", required = false) MultipartFile updateProfile
  ) {
    return ResponseEntity.ok(userService.update(userId, userUpdateDto, updateProfile));
  }

  @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
  @DeleteMapping(path = "/{userId}")
  public ResponseEntity<UserResponseDto> deleteUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.delete(userId));
  }
}

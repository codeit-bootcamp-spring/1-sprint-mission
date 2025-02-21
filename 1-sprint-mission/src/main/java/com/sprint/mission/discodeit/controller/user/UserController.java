package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.*;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDTO;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  public UserController(UserService userService, UserStatusService userStatusService) {
    this.userService = userService;

    this.userStatusService = userStatusService;
  }

  //사용자 등록(201 ok)
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponseDTO> createUser(
      @RequestPart(name = "user-create-dto") @Valid UserCreateDTO userCreateDTO,
      @RequestPart(name = "binary-content-create-request") MultipartFile binaryContentCreateRequest
  ) {
    BinaryContentCreateRequest binaryContent = null;
    if (Objects.nonNull(binaryContentCreateRequest)) {
      try {
        binaryContent = new BinaryContentCreateRequest(
            binaryContentCreateRequest.getOriginalFilename(),
            binaryContentCreateRequest.getContentType(),
            binaryContentCreateRequest.getBytes()
        );
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }

    UserResponseDTO creatUser = userService.create(userCreateDTO, binaryContent);
    return ResponseEntity.status(HttpStatus.CREATED).body(creatUser);
  }

  //사용자 정보 수정 (200 ok)
  @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponseDTO> updateUser(
      @RequestPart(name = "user-update-dto") @Valid UserUpdateDTO updateDTO,
      @RequestPart("binary-content-update-request") MultipartFile binaryContentUpdateRequest
  ) {
    BinaryContentCreateRequest binaryContent = null;
    if (Objects.nonNull(binaryContentUpdateRequest)) {
      try {
        binaryContent = new BinaryContentCreateRequest(
            binaryContentUpdateRequest.getOriginalFilename(),
            binaryContentUpdateRequest.getContentType(),
            binaryContentUpdateRequest.getBytes()
        );
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }
    UserResponseDTO updateUser = userService.update(updateDTO, binaryContent);
    return ResponseEntity.ok(updateUser);
  }

  //사용자 삭제 (204 No Content)
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  //모든 사용자 조회
  @GetMapping("/findAll")
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    List<UserResponseDTO> allUser = userService.findAll();
    return ResponseEntity.ok(allUser);
  }

  //사용자의 온라인 상태 업데이트
  @PatchMapping("/{userId}/status")
  public ResponseEntity<UserStatusResponseDTO> updateUserStatus(@PathVariable UUID userId) {
    UserStatusResponseDTO status = userStatusService.updateByUserId(userId);
    return ResponseEntity.ok(status);
  }


}

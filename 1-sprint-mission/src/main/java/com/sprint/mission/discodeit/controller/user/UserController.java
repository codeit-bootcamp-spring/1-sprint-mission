package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.converter.ToBinaryContentConverter;
import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.*;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDTO;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(name = "User Controller", description = "사용자 관련 API 엔드포인트 관리")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final ToBinaryContentConverter toBinaryContentConverter;

  public UserController(UserService userService, UserStatusService userStatusService,
      ToBinaryContentConverter toBinaryContentConverter) {
    this.userService = userService;

    this.userStatusService = userStatusService;
    this.toBinaryContentConverter = toBinaryContentConverter;
  }

  @Operation(summary = "사용자 등록", description = "새로운 사용자 등록 및 사용자 정보 반환")
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<UserResponseDTO> createUser(
      @RequestPart("userCreateRequest") @Valid UserCreateDTO userCreateDTO,
      @RequestPart(name = "binary-content-create-request", required = false) MultipartFile binaryContentCreateRequest
  ) {
    BinaryContentCreateRequest binaryContent = toBinaryContentConverter.convert(
        binaryContentCreateRequest);
    UserResponseDTO creatUser = userService.create(userCreateDTO, binaryContent);
    return ResponseEntity.status(HttpStatus.CREATED).body(creatUser);
  }

  @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 및 수정된 사용자 정보 반환")
  @PatchMapping(path = "{userId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable("userId") UUID userId,
      @RequestPart(name = "userUpdateRequest") @Valid UserUpdateDTO updateDTO,
      @RequestPart(value = "binary-content-update-request", required = false) MultipartFile binaryContentUpdateRequest
  ) {
    BinaryContentCreateRequest binaryContent = toBinaryContentConverter.convert(
        binaryContentUpdateRequest);
    UserResponseDTO updateUser = userService.update(userId, updateDTO, binaryContent);
    return ResponseEntity.ok(updateUser);
  }

  @Operation(summary = "사용자 삭제", description = "사용자 삭제")
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "모든 사용자 조회", description = "모든 사용자 목록을 조회 후 반환")
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> findAll() {
    List<UserResponseDTO> allUser = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(allUser);
  }

  @Operation(summary = "사용자 상태 업데이트", description = "사용자의 온라인 상태를 업데이트 및 상태 정보를 반환")
  @PatchMapping("{userId}/userStatus")
  public ResponseEntity<UserStatusResponseDTO> updateUserStatus(@PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateDTO request) {
    UserStatusResponseDTO updateStatus = userStatusService.updateByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updateStatus);
  }

}

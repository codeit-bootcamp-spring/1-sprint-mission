package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.ApiResponse;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원 목록 조회", description = "전체 회원 조회")
  @GetMapping
  public ResponseEntity<List<UsersDto>> listUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @Operation(summary = "회원 상세 조회", description = "단일 회원 조회")
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.find(id));
  }

  @Operation(summary = "회원 가입", description = "회원 가입")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UserDto>> registerUser(@Valid
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    log.info("[회원가입 요청] userCreateRequest: name={}, email={}, password={}",
        userCreateRequest.getUsername(), userCreateRequest.getEmail(),
        userCreateRequest.getPassword());

    UserDto userDTO = UserDto.builder()
        .name(userCreateRequest.getUsername())
        .email(userCreateRequest.getEmail())
        .password(userCreateRequest.getPassword())
        .build();

    UserDto createdUser = userService.createWithProfileImage(userDTO, profile);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "회원가입에 성공했습니다.", createdUser));
  }

  @Operation(summary = "회원 정보 수정", description = "회원 정보 수정")
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UsersDto>> updateUser(
      @PathVariable UUID id,
      @RequestPart("user") UsersDto usersDto,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    UsersDto updatedUser = userService.update(id, usersDto, profile);
    return ResponseEntity.ok(new ApiResponse<>(true, "회원정보가 수정되었습니다.", updatedUser));
  }

  @Operation(summary = "유저 삭제", description = "회원 정보 삭제")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "회원정보가 삭제되었습니다."));
  }

  @Operation(summary = "상태 업데이트", description = "사용자의 온라인 상태 업데이트")
  @PatchMapping("/{id}/online-status")
  public ResponseEntity<ApiResponse<Void>> updateOnlineStatus(
      @PathVariable UUID id,
      @RequestParam boolean status) {

    userService.updateOnlineStatus(id, status);
    return ResponseEntity.ok(new ApiResponse<>(true, "상태가 업데이트되었습니다."));
  }
}

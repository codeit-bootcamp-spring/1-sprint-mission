package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  //사용자 단일 조회
  @GetMapping("/{userId}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String userId,
      @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

    UserResponseDto userDto = userService.findById(userId);
    String etag = "\"" + userDto.hashCode() + "\"";
    if (etag.equals(ifNoneMatch)) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(userDto);
    }
    return ResponseEntity.ok(userDto);
  }

  //사용자 등록
  //InvalidContentTypeException 예외처리해야 -> 예외처리 없이 application/json 으로 보냈을때도 받을 수 있게 할 수는 없을까?
  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(
      @RequestPart(value = "profile", required = false) MultipartFile file,
      @RequestPart("userCreateRequest") CreateUserDto createUserDto) {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(createUserDto));
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(createUserDto, file));
  }

  //사용자 정보 수정
  @PatchMapping("/{userId}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable String userId,
      @RequestPart("userUpdateRequest") UpdateUserDto updateUserDto,
      @RequestPart(value = "profile", required = false) MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return ResponseEntity.ok(userService.updateUser(userId, updateUserDto));
    }
    return ResponseEntity.ok(userService.updateUser(userId, updateUserDto, file));
  }

  //사용자 삭제
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable String userId) {
    if (userService.deleteUser(userId)) {
      return ResponseEntity.ok().body("User deleted");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }


  @PatchMapping("/{userId}/userStatus")
  public UserStatusResponseDto updateUserStatus(@PathVariable String userId) {
    return userStatusService.updateByUserId(userId, new UpdateUserStatusDto(Instant.now()));
  }


  //모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
  }
}

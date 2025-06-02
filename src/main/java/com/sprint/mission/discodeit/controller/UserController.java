package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;

  //사용자 단일 조회
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> getUser(@PathVariable String userId) {

    UserDto userDto = userService.findById(userId);

    return ResponseEntity.ok(userDto);
  }

  //사용자 등록
  //InvalidContentTypeException 예외처리해야 -> 예외처리 없이 application/json 으로 보냈을때도 받을 수 있게 할 수는 없을까?
  @PostMapping
  public ResponseEntity<UserDto> createUser(
      @RequestPart(value = "profile", required = false) MultipartFile file,
      @Valid @RequestPart("userCreateRequest") CreateUserDto createUserDto) {

    log.info("사용자 생성 요청: {}", createUserDto);
    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(createUserDto));
    }
    try {
      UserDto userDto = userService.create(createUserDto, file);
      return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    } catch (Exception e) {
      log.error("사용자 생성 중 오류 발생: {}", e);
      throw e;
    }
  }

  //사용자 정보 수정
  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
  @PatchMapping("/{userId}")
  public ResponseEntity<UserDto> updateUser(@PathVariable String userId,
      @Valid @RequestPart("userUpdateRequest") UpdateUserDto updateUserDto,
      @RequestPart(value = "profile", required = false) MultipartFile file) {

    log.info("사용자 수정 요청: userId = {}, updateUserDto = {}", userId, updateUserDto);
    try {
      if (file == null || file.isEmpty()) {
        UserDto userDto = userService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok(userDto);
      } else {
        UserDto userDto = userService.updateUser(userId, updateUserDto, file);
        return ResponseEntity.ok(userDto);
      }
    } catch (Exception e) {
      log.error("사용자 수정 중 오류 발생: {}", e.getMessage());
      throw e;
    }
  }

  //사용자 삭제
  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
  public ResponseEntity<String> deleteUser(@PathVariable String userId) {
    log.info("사용자 삭제 요청: userId = {}", userId);
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok().body("User deleted");
    } catch (Exception e) {
      log.error("사용자 삭제 중 오류 발생: {}", e.getMessage());
      throw e;
    }
  }

  //모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers() {
    return ResponseEntity.ok(userService.findAll());
  }
}

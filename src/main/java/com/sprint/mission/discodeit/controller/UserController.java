package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  
  private final UserService userService;

  // 사용자 등록
  @PostMapping
  public ResponseEntity<UserDto> createUser(
      @RequestPart @Valid UserRequest userRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);

    log.info("Received Created user with name: {}", userRequest.name());
    UserDto createdUser = userService.createUser(userRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  // 사용자 정보 수정
  @PatchMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable("id") UUID id,
      @RequestPart @Valid UserUpdateDTO userUpdateDTO,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    log.info("Received Updating user with ID : {}", id);
    Optional<BinaryContentRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO, profileRequest));
  }

  // 사용자 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
    log.info("Received Delete user with ID : {}", id);
    userService.deleteUser(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
  }

  // 모든 사용자 조회
  @GetMapping
  public ResponseEntity<List<UserDto>> findAllUser() {
    List<UserDto> userDtos = userService.findAllUserDTO();
    return ResponseEntity.ok(userDtos); // 상태 코드 200과 함께 사용자 목록 반환
  }

  // 사용자 ID로 조회
  @GetMapping("/{id}")
  public ResponseEntity<UserDto> findUserById(@PathVariable("id") UUID id) {
    UserDto userDto = userService.findUserDTO(id);
    return ResponseEntity.ok(userDto); // 상태 코드 200과 함께 사용자 정보 반환
  }

  // 사용자 온라인 상태 업데이트
  @PatchMapping("/{id}/online")
  public ResponseEntity<UserStatusUpdateDTO> updateUserStatus(@PathVariable("id") UUID id,
      @RequestBody UserStatusUpdateDTO userStatusUpdateDTO) {
    UserStatusUpdateDTO updatedStatus = userService.updateUserStatus(id, userStatusUpdateDTO);
    return ResponseEntity.status(HttpStatus.OK).body(updatedStatus);
  }

  private Optional<BinaryContentRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentRequest binaryContentCreateRequest = new BinaryContentRequest(
            profileFile.getOriginalFilename(),
            profileFile.getSize(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}


package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.apidocs.UserApiDocs;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequst;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApiDocs {

  private final UserService userService;
  private final UserStatusService userStatusService;

  //사용자 다중 조회
  @GetMapping()
  @Override
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  //사용자 생성
  @PostMapping()
  @Override
  public ResponseEntity<User> create(
      @RequestPart("user") @Valid UserCreateRequst userCreateRequst,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentCreateRequest> profileDto = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileDto);
    User createdUser = userService.create(userCreateRequst, profileDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }


  //사용자 수정
  @PatchMapping("/{userId}")
  @Override
  public ResponseEntity<User> update(
      @PathVariable UUID userId,
      @Valid @RequestPart("user") UserUpdateRequest userUpdateRequest,
      @Valid @RequestPart(value = "profile", required = false) MultipartFile profile) {
    Optional<BinaryContentCreateRequest> profileDto = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileDto);
    User updatedUser = userService.update(userId, userUpdateRequest, profileDto);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
  }

  //사용자 삭제
  @DeleteMapping("/{userId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.deleteById(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //응답 데이터에 정보가 없음
  }


  //사용자 온라인 상태 업데이트
  @PatchMapping("/{userId}/userStatus")
  @Override
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest status) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, status);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
  }

  //프로필 이미지 처리 메서드
  private Optional<BinaryContentCreateRequest> resolveProfileDto(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException("파일을 처리하는 동안 오류 발생", e);
      }
    }
  }
}

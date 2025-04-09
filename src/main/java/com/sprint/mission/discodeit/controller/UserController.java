package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.validator.ProfileFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<UserDto> create(
      @ModelAttribute UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false)
      @ProfileFile(message = "이미지 파일만 설정할 수 있습니다.")
      MultipartFile profile
  ) {
    UserDto createdUser = userService.create(userCreateRequest, Optional.ofNullable(profile));

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }

  @PatchMapping(
      value = "/{user-id}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  /*
  TODO @PathVariable은 URL 경로의 일부로 데이터를 전달하는 방식.
   multipart/form-data 요청에서는 URL이 아니라 요청 바디에서 데이터가 전달되기 때문에, 스프링이 @PathVariable을 제대로 매핑하지 못할 수도 있음.
   그래서 보통 파일 업로드가 포함된 경우 (이렇게 form data로 줘야하는 경우) @RequestParam을 사용해서 바디에서 값을 받아야 함.
   @RequestParam : multipart/form-data 요청에서는 URL이 아니라 "바디에서" 값을 받는다!
    -> GET 요청이면 URL에서 (?userId=...) 값을 받지만,
    -> POST (multipart/form-data) 요청이면 바디에서 값을 받는다!
   */
  public ResponseEntity<UserDto> update(
      @PathVariable("user-id") UUID userId,
      @ModelAttribute UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false)
      @ProfileFile(message = "이미지 파일만 설정할 수 있습니다.")
      MultipartFile profile
  ) {

    UserDto updatedUser = userService.update(userId, userUpdateRequest,
        Optional.ofNullable(profile));
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUser);
  }

  @DeleteMapping("/{user-id}")
  public ResponseEntity<Void> delete(@PathVariable("user-id") UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> allUsers = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(allUsers);
  }

  @PostMapping("/{user-id}/user-status")
  public ResponseEntity<UserStatusDto> createUserStatusByUserId(
      @PathVariable("user-id") UUID userId,
      @RequestBody UserStatusCreateRequest userStatusCreateRequest
  ) {
    UserStatusDto userStatusDto = userStatusService.create(userStatusCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userStatusDto);
  }

  @PatchMapping("/{user-id}/user-status")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable("user-id") UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatusDto); // build()는 응답 데이터 없이 상태코드만 반환할 때 사용.
  }

  private Optional<BinaryContentDto> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      BinaryContentDto binaryContentCreateRequest = new BinaryContentDto(
          UUID.randomUUID(),
          profileFile.getOriginalFilename(),
          (int) profileFile.getSize(), //getSize() -> long으로 반환
          profileFile.getContentType()
      );
      return Optional.of(binaryContentCreateRequest);
    }
  }
}

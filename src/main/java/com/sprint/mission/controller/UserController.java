package com.sprint.mission.controller;

import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final JCFUserService userService;
  private final UserStatusService userStatusService;

  @RequestMapping(path = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> create(@RequestPart("createRequestDto") UserDtoForCreate requestDTO,
      @RequestPart("profile") MultipartFile profile) {
    Optional<BinaryContentDto> binaryContentDto = BinaryContentDto.fileToBinaryContentDto(profile);
    userService.create(requestDTO, binaryContentDto);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("User created successfully");
  }

  @PatchMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> update(@PathVariable("id") UUID userId,
      @RequestPart("dto") UserDtoForUpdate requestDTO) {

    userService.update(userId, requestDTO);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body("Successfully updated");
  }


  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @PatchMapping("{id}/status")
  public ResponseEntity<String> updateStatusByUserId(@PathVariable("id") UUID userId) {
    userStatusService.updateByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body("Successfully updated");
  }


  @GetMapping
  public ResponseEntity<List<FindUserDto>> findAll() {
    Map<User, UserStatus> statusMapByUser = userStatusService.findStatusMapByUserList();

    List<FindUserDto> findUserDtos = statusMapByUser.keySet().stream()
        .map(user -> {
          return FindUserDto.fromEntityAndStatus(user, statusMapByUser.get(user).isOnline());
        }).toList();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(findUserDtos);
  }
}

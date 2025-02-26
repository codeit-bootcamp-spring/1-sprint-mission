package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 API")
public class UserController {

  private final UserService userService;

  //사용자 단일 조회
  @GetMapping("/{userId}")
  public UserResponseDto getUser(@PathVariable String userId) {
    return userService.findById(userId);
  }

  //사용자 등록
  //InvalidContentTypeException 예외처리해야 -> 예외처리 없이 application/json 으로 보냈을때도 받을 수 있게 할 수는 없을까?
  @PostMapping
  public UserResponseDto createUser(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart("user") CreateUserDto createUserDto) {
    if (file == null || file.isEmpty()) {
      return userService.create(createUserDto);
    }
    return userService.create(createUserDto, file);
  }

  //사용자 정보 수정
  @PutMapping("/{userId}")
  public UserResponseDto updateUser(@PathVariable String userId,
      @RequestPart("user") UpdateUserDto updateUserDto,
      @RequestPart(value = "file", required = false) MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return userService.updateUser(userId, updateUserDto);
    }

    return userService.updateUser(userId, updateUserDto, file);
  }

  //사용자 삭제
  @DeleteMapping("/{userId}")
  public String deleteUser(@PathVariable String userId) {
    if (userService.deleteUser(userId)) {
      return "User deleted successfully";
    }
    return "Delete user failed";
  }

  //모든 사용자 조회
  @GetMapping
  public List<UserResponseDto> getUsers() {
    return userService.findAll();
  }

}

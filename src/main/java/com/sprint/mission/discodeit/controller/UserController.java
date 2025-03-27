package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;


  //등록
  @PostMapping
  public String addUser(@RequestBody UserCreateDTO userCreateDTO) {
    userService.createUser(userCreateDTO);
    return "User: " + userCreateDTO.name() + " created";
  }

  //정보수정
  @PatchMapping("/{id}")
  public String updateUser(@PathVariable("id") UUID id, @RequestBody UserUpdateDTO userUpdateDTO) {
    userService.updateUser(id, userUpdateDTO);
    return "User updated";
  }

  //사용자 삭제
  @DeleteMapping("/{id}")
  public String deleteUser(@PathVariable("id") UUID id) {
    userService.deleteUser(id);
    return "User deleted";
  }

  //모든 사용자 조회
  @GetMapping
  public List<UserDto> findAllUser() {
    return userService.findAllUserDTO();
  }

  //아이디로 사용자 조회
  @GetMapping("/{id}")
  public UserDto findUserById(@PathVariable("id") UUID id) {
    return userService.findUserDTO(id);
  }

  //사용자 온라인상태 업데이트
  @PatchMapping("/{id}/online")
  public UserStatusUpdateDTO updateUserStatus(@PathVariable("id") UUID id,
      @RequestBody UserStatusUpdateDTO userStatusUpdateDTO) {
    return userService.updateUserStatus(id, userStatusUpdateDTO);
  }
}

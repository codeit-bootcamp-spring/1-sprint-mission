package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.facade.user.UserMasterFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserMasterFacade userFacade;

  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
    UserResponseDto user = userFacade.findUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public ResponseEntity<UserResponseDto> createUser(@Valid @ModelAttribute CreateUserRequest userDto) {
    UserResponseDto user = userFacade.createUser(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public ResponseEntity<List<UserResponseDto>> getUsers(Model model) {
    List<UserResponseDto> users = userFacade.findAllUsers();
    return ResponseEntity.ok(users);
  }


  @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @Valid @ModelAttribute UserUpdateDto userDto) {
    UserResponseDto user = userFacade.updateUser(id, userDto);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<String> deleteUser(@PathVariable String id, @RequestParam String password) {
    try {
      userFacade.deleteUser(id, password);
      return ResponseEntity.ok("Successfully Deleted");
    } catch (UserValidationException e) {
      return ResponseEntity.badRequest().body("Failed To Delete");
    }
  }

  @RequestMapping(value = "/user-form", method = RequestMethod.GET)
  public String getUserForm() {
    return "user/userForm";
  }

  @RequestMapping(value = "/user-update-form/{id}", method = RequestMethod.GET)
  public String getUpdateUserForm(@PathVariable String id, Model model) {
    UserResponseDto user = userFacade.findUserById(id);
    model.addAttribute("user", user);
    return "user/userUpdateForm";
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.facade.user.UserMasterFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserMasterFacade userFacade;

  @RequestMapping(value = "/user-form", method = RequestMethod.GET)
  public String getUserForm() {
    return "user/userForm";
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String createUser(@Valid @ModelAttribute CreateUserRequest userDto, Model model) {
    UserResponseDto user = userFacade.createUser(userDto);
    model.addAttribute("user", user);
    return "user/userInfo";
  }

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public String getUsers(Model model) {
    List<UserResponseDto> users = userFacade.findAllUsers();

    model.addAttribute("users", users);
    return "user/users";
  }

  @RequestMapping(value = "/user-update-form/{id}", method = RequestMethod.GET)
  public String getUpdateUserForm(@PathVariable String id, Model model) {
    UserResponseDto user = userFacade.findUserById(id);
    model.addAttribute("user", user);
    return "user/userUpdateForm";
  }


  @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
  public String updateUser(@PathVariable String id, @Valid @ModelAttribute UserUpdateDto userDto, Model model) {
    UserResponseDto user = userFacade.updateUser(id, userDto);

    model.addAttribute("user", user);
    return "redirect:/api/v1/users";
  }

}

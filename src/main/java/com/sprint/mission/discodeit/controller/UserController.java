package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
  private final UserService userService;

  @RequestMapping(value = "/user-form", method = RequestMethod.GET)
  public String getUserForm(){
    return "user/userForm";
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String createUser(@Valid @ModelAttribute CreateUserRequest userDto, Model model){

    UserResponseDto user = userService.createUser(userDto);
    model.addAttribute("user", user);

    log.info("encoded data: {}", user.profilePictureBase64());
    return "user/userInfo";
  }
}

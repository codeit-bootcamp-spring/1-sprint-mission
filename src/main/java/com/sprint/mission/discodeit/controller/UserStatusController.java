package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.UserStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @RequestMapping(value = "/user/{id}/status", method = RequestMethod.POST)
  public String updateUserStatus(@PathVariable String id, @RequestBody UpdateUserStatusDto userStatusDto){

    userStatusService.updateByUserId(id, userStatusDto);

    return "redirect:/api/v1/users";
  }
}

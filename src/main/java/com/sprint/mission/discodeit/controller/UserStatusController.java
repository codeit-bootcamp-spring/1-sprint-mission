package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/status")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @PutMapping("/{userId}")
    public UserStatusResponseDto getUserStatus(@PathVariable String userId, @RequestBody UpdateUserStatusDto updateUserStatusDto) {
        return userStatusService.updateByUserId(userId, updateUserStatusDto);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-status")
public class UserStatusController {
    private final UserStatusService userStatusService;

    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    // 사용자 마지막 활동 시간 업데이트 API (PATCH)
    @RequestMapping(value = "/user/{userId}/last-active", method = RequestMethod.PATCH)
    public UserStatus updateUserLastActive(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request) {
        return userStatusService.updateLastActiveAt(userId, request.newLastActiveAt());
    }

}

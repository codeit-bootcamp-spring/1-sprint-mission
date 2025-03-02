package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userStatus")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/updateOnline", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusResponse> updateOnlineStatus(@RequestParam("userid") UUID userId) {
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(Instant.now());
        UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
        if (updatedUserStatus == null) {
            throw new NoSuchElementException("사용자 상태를 찾을 수 없습니다: " + userId);
        }
        UserStatusResponse response = UserStatusResponse.from(updatedUserStatus);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<UserStatusResponse> checkUserStatus(@RequestParam("userid") UUID userId) {
        UserStatus userStatus = userStatusService.findByUserId(userId);
        if (userStatus == null) {
            throw new NoSuchElementException("사용자 상태를 찾을 수 없습니다: " + userId);
        }
        UserStatusResponse response = UserStatusResponse.from(userStatus);
        return ResponseEntity.ok(response);
    }
}
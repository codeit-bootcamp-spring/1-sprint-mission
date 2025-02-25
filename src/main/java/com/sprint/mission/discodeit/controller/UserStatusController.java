package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/userStatuses")
@RequiredArgsConstructor
public class UserStatusController {
    private final UserStatusService userStatusService;

    @GetMapping("/user")
    public ResponseEntity<UserStatus> getStatusByUserId(@RequestParam("id") UUID userId) {
        UserStatus userStatus = userStatusService.findByUserId(userId);
        return ResponseEntity.ok(userStatus);
    }

    @GetMapping
    public ResponseEntity<UserStatus> getStatusById(@RequestParam("id") UUID id) {
        UserStatus userStatus = userStatusService.find(id);
        return ResponseEntity.ok(userStatus);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserStatus>> getAll() {
        List<UserStatus> all = userStatusService.findAll();
        return ResponseEntity.ok(all);
    }
}

package com.sprint.mission.discodeit.controller.user;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-status")
public class UserStatusController {

    private UserStatusService userStatusService;

    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStatus(
            @PathVariable(name = "id") UUID userId,
            @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
    ) {
        userStatusService.update(userId, userStatusUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}

package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCleanupListener {
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    @EventListener
    public void handleUserDeleted(UserDeletedEvent event) {
        if (event.profileImageId() != null) {
            binaryContentService.delete(event.profileImageId());
        }
        if (event.userStatusId() != null) {
            userStatusService.delete(event.userStatusId());
        }
    }
}

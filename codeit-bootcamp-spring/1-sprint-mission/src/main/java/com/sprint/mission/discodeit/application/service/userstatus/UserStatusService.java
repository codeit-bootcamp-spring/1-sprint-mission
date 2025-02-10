package com.sprint.mission.discodeit.application.service.userstatus;

import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.enums.OnlineStatus;
import com.sprint.mission.discodeit.repository.userstatus.interfaces.UserStatusRepository;
import java.util.UUID;

public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserService userService;

    public UserStatusService(
            UserStatusRepository userStatusRepository,
            UserService userService
    ) {
        this.userStatusRepository = userStatusRepository;
        this.userService = userService;
    }

    public UserStatus findOneByUser(User user) {
        return userStatusRepository.findByUser(user).orElse(new UserStatus(user));
    }

    public void updateLastAccessTime(UUID userId) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        UserStatus foundUserStatus = findOneByUser(foundUser);
        foundUserStatus.updateLastAccessedAt();
        userStatusRepository.save(foundUserStatus);
    }

    public OnlineStatus getUserStatus(UUID userId) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        UserStatus foundUserStatus = findOneByUser(foundUser);
        return foundUserStatus.getOnlineStatus();
    }
}

package com.sprint.mission.discodeit.application.service.userstatus;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.enums.OnlineStatus;
import com.sprint.mission.discodeit.repository.userstatus.interfaces.UserStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;

    public UserStatusService(
            UserStatusRepository userStatusRepository
    ) {
        this.userStatusRepository = userStatusRepository;
    }

    public UserStatus createAtFirstJoin(User user) {
        if (userStatusRepository.isExistUser(user)) {
            throw new IllegalArgumentException("userStatus already exist");
        }
        return userStatusRepository.save(new UserStatus(user));
    }

    public UserStatus findOneByUser(User user) {
        return userStatusRepository.findByUser(user).orElse(new UserStatus(user));
    }

    public void updateLastAccessTime(User user) {
        UserStatus foundUserStatus = findOneByUser(user);
        foundUserStatus.updateLastAccessedAt();
        userStatusRepository.save(foundUserStatus);
    }

    public OnlineStatus getUserOlineStatus(User user) {
        UserStatus foundUserStatus = findOneByUser(user);
        return foundUserStatus.getOnlineStatus();
    }

    public void delete(User targetUser) {
        userStatusRepository.deleteByUser(targetUser);
    }
}

package discodeit.service;

import discodeit.dto.userstatus.CreateUserStatusRequest;
import discodeit.dto.userstatus.UpdateUserStatusRequest;
import discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusRequest createUserStatusRequest);
    UserStatus find(UUID userStatusId);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    UserStatus update(UUID userStatusId, UpdateUserStatusRequest updateUserStatusRequest);
    UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest updateUserStatusRequest);
    void delete(UUID userStatusId);
}

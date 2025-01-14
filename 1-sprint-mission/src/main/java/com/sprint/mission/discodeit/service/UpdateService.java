package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface UpdateService {
    void updateUser(UUID userUuid, String newUserId, String newPassword);
    void updateMessage(UUID messageUuid, String newUserMessage);
    void updateChannel(UUID channelUuid, String newChannelTitle);
}
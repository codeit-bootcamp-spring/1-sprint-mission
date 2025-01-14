package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface DeleteService {
    // UUID로 확인 후 삭제
    void deleteUser(UUID userUuid);
    void deleteMessage(UUID messageUuid);
    void deleteChannel(UUID channelUuid);
}

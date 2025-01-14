package com.sprint.mission.discodeit.service;

import java.util.UUID;
import java.util.Map;

public interface SearchService {
    // 개별 탐색
    void searchUserByUUID(UUID userUuid);

    void searchMessageByUUID(UUID messageUuid);

    void searchChannelByUUID(UUID channelUuid);

    //전체 탐색
    void searchAllUsers();


    void searchAllChannels();


}
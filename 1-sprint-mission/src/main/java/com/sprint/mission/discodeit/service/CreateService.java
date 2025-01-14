package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface CreateService {

    void createUser(UUID userUuid, String userId, String userPassword);
    void createChannel(UUID chanelUuid, String channelTitle, String userId);
    void createMessage(UUID messageUuid , String userId, String text);



}
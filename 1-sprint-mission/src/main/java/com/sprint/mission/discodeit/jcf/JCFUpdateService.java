package com.sprint.mission.discodeit.jcf;


import com.sprint.mission.discodeit.entity.Data;
import com.sprint.mission.discodeit.service.UpdateService;

import java.util.Map;
import java.util.UUID;

public class JCFUpdateService implements UpdateService {

    private final Data data;

    public JCFUpdateService(Data data) {
        this.data = data;
    }


    //UUID로 Data 객체의 value값 얻고 put 으로 update
    @Override
    public void updateUser(UUID userUuid, String newUserId, String newPassword) {
        Map<String, String> user = data.getUser(userUuid);
        if (user != null) {
            user.put("userId", newUserId);
            user.put("userPassword", newPassword);
            System.out.println("User updated: " + newUserId);
        } else {
            System.out.println("User not found with UUID: " + userUuid);
        }
    }

    @Override
    public void updateMessage(UUID messageUuid, String newUserMessage) {
        Map<String, String> message = data.getMessage(messageUuid);
        if (message != null) {
            message.put("userMessage", newUserMessage);
            System.out.println("Message updated: " + newUserMessage);
        } else {
            System.out.println("Message not found with UUID: " + messageUuid);
        }
    }

    @Override
    public void updateChannel(UUID channelUuid, String newChannelTitle) {
        Map<String, String> channel = data.getChannel(channelUuid);
        if (channel != null) {
            channel.put("channelTitle", newChannelTitle);
            System.out.println("Channel updated: " + newChannelTitle);
        } else {
            System.out.println("Channel not found with UUID: " + channelUuid);
        }
    }
}
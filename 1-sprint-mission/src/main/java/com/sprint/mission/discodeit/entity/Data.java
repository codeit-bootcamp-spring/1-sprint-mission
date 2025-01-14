package com.sprint.mission.discodeit.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data {
    // UUID -> <userId, userPassword>
    protected final Map<UUID, Map<String, String>> userData;
    //UUID -> <userId, userMessage>
    protected final Map<UUID, Map<String, String>> messageData;
    //UUID -> <channelTitle, userId>
    protected final Map<UUID, Map<String, String>> channelData;



    public Data() {
        this.userData = new HashMap<>();
        this.channelData = new HashMap<>();
        this.messageData = new HashMap<>();
    }
    // Add user
    public void addUser(UUID userUuid, String userId, String userPassword) {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("userPassword", userPassword);
        userData.put(userUuid, userInfo);
    }

    // Add channel
    public void addChannel(UUID channelUuid, String channelTitle, String userId) {
        Map<String, String> channelInfo = new HashMap<>();
        channelInfo.put("channelTitle", channelTitle);
        channelInfo.put("userId", userId);
        channelData.put(channelUuid, channelInfo);
    }

    // Add message
    public void addMessage(UUID messageUuid, String userId, String userMessage) {
        Map<String, String> messageInfo = new HashMap<>();
        messageInfo.put("userId", userId);
        messageInfo.put("userMessage", userMessage);
        messageData.put(messageUuid, messageInfo);
    }

    // Get 메소드
    public Map<String, String> getMessage(UUID messageUuid) {
        return messageData.getOrDefault(messageUuid, null);
    }

    public Map<String, String> getUser(UUID userUuid) {
        return userData.getOrDefault(userUuid, null);
    }

    public Map<String, String> getChannel(UUID channelUuid) {
        return channelData.getOrDefault(channelUuid, null);
    }

    public Map<UUID, Map<String, String>> getAllUserData() {
        return new HashMap<>(userData);
    }


    public Map<UUID, Map<String, String>> getAllMessageData() {
        return new HashMap<>(messageData);
    }


    public Map<UUID, Map<String, String>> getAllChannelData() {
        return new HashMap<>(channelData);
    }



    //사용자 존재 여부 확인 (userId 기반)
    public boolean containsUserId(String userId){
        return userData.values().stream()
                .anyMatch(user -> user.get("userId").equals(userId));
    }
    //사용자 존재 여부 확인 (UUID 기반)
    public boolean containsMessageUUID(UUID messageUuid) {
        return userData.containsKey(messageUuid);
    }
    public boolean containsChannelUUID(UUID channelUuid) {
        return userData.containsKey(channelUuid);
    }

    // 삭제 메소드
    public void removeUser(UUID userUuid) {
        if (userData.containsKey(userUuid)) {
            userData.remove(userUuid);
            System.out.println("com.sprint.mission.discodeit.entity.User with UUID " + userUuid + " has been removed.");
        } else {
            System.out.println("com.sprint.mission.discodeit.entity.User with UUID " + userUuid + " not found.");
        }
    }
    public void removeMessage(UUID messageUuid) {
        if (messageData.containsKey(messageUuid)) {
            messageData.remove(messageUuid);
            System.out.println("Message with UUID " + messageUuid + " has been removed.");
        } else {
            System.out.println("Message with UUID " + messageUuid + " not found.");
        }
    }
    public void removeChannel(UUID channelUuid) {
        if (channelData.containsKey(channelUuid)) {
            channelData.remove(channelUuid);
            System.out.println("Channel with UUID " + channelUuid + " has been removed. ");
        } else {
            System.out.println("Channel with UUID " + channelUuid + " not found.");
        }
    }






}
package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Data;
import com.sprint.mission.discodeit.service.CreateService;

import java.util.UUID;

public class JCFCreateService implements CreateService {
    private final Data data;

    public JCFCreateService(Data data){
        this.data = data;
    }

    // 생성
    @Override
    public void createUser(UUID userUuid, String userId, String userPassword){
        if(!data.containsUserId(userId)){
            data.addUser( userUuid,userId, userPassword);
            System.out.println("User created: " + userId + " -> " + userPassword);
        }else{
            System.out.println("User already exists: " + userUuid);
        }

    }

    @Override
    public void createMessage(UUID messageUuid, String userId, String userMessage) {
        if (data.containsMessageUUID(messageUuid)) {
            data.addMessage(messageUuid, userId, userMessage);
            System.out.println("Message created >>>> " + userId + " : " + userMessage);
        } else {
            System.out.println("User not found: " + userId);
        }
    }

    @Override
    public void createChannel(UUID channelUuid, String channelTitle, String userId) {
        if (data.containsChannelUUID(channelUuid)) {
            data.addChannel(channelUuid, channelTitle, userId);
            System.out.println("Channel created >>>> " + channelTitle + " for user " + userId);
        } else {
            System.out.println("User not found: " + userId);
        }
    }





}
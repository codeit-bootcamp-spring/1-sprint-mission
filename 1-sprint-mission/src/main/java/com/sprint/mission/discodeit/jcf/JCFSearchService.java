package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Data;
import com.sprint.mission.discodeit.service.SearchService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class JCFSearchService implements SearchService {
    final private Data data;

    public JCFSearchService(Data data){
        this.data = data;
    }

    // 개별 데이터 검색
    @Override
    public void searchUserByUUID(UUID userUuid) {
        Map<String, String> user = data.getUser(userUuid);

        if (user != null) {
            System.out.println("  << User found >> ");
            System.out.println( "  "+ user.get("userId"));

        } else {
            System.out.println("User not found with UUID: " + userUuid);

        }
    }

    @Override
    public void searchMessageByUUID(UUID messageUuid) {
        Map<String, String> message = data.getMessage(messageUuid);
        if (message != null) {
            String userMessage = message.get("userMessage");
            System.out.println("  << Message found >> ");
            System.out.println("  "+userMessage);

        } else {
            System.out.println("Message not found with UUID: " + messageUuid);

        }
    }

    @Override
    public void searchChannelByUUID(UUID channelUuid) {
        Map<String, String> channel = data.getChannel(channelUuid);
        if (channel != null) {
            String channelName = channel.get("channelTitle");
            System.out.println("  << Channel found >> ");
            System.out.println("  "+ channelName);

        } else {
            System.out.println("Channel not found with UUID: " + channelUuid);
        }
    }

    // 전체 데이터 검색
    @Override
    public void searchAllUsers() {
        Map<UUID, Map<String, String>> allUsers = data.getAllUserData();
        ArrayList<String> users = new ArrayList<>();
        allUsers.forEach((uuid, details) -> {
            String userId = details.get("userId");
            users.add(userId);
        } );
        System.out.println("  << All Users found >>  ");
        System.out.println("  " + users);
    }



    @Override
    public void searchAllChannels() {
        Map<UUID, Map<String, String>> allChannel = data.getAllChannelData();
        ArrayList<String>channels = new ArrayList<>();
        allChannel.forEach((uuid, details) -> {
            String userId = details.get("channelTitle");
            channels.add(userId);
        } );
        System.out.println("  << All Channel found >>  ");
        System.out.println("  " + channels);

    }



}
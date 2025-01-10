package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private final List<Channel> channelList;
    private String userName;
    private String userEmail;

    //생성자
    public User(String userName, String userEmail) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        channelList = new ArrayList<>();
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return userName;
    }

    public String getEmail() {
        return userEmail;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    private void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    //유저 이름 수정
    public void updateName(String userName) {
        this.userName = userName;
        updateUpdatedAt();
    }

    //유저 이메일 수정
    public void updateEmail(String email) {
        this.userEmail = email;
        updateUpdatedAt();
    }

    //유저에 Channel 추가하기
    public void addChannel(Channel newChannel) {
        channelList.add(newChannel);
    }

    public void removeChannel(Channel removeChannel) {
        channelList.remove(removeChannel);
    }

    @Override
    public String toString(){
        return "User{name:" + userName + ",email:" + userEmail
                + ",channelList:" + channelList + ",createdAt:" + createdAt
                + ",updateAt:" + updatedAt + "}";
    }
}
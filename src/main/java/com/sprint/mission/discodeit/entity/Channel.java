package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Channel extends BaseObject {
    private String channelName;
    private User channelOwnerUser;
    private List<User> channelUsers;

    public Channel(String channelName, User channelOwnerUser, List<User> channelUsers) {
        super();
        setChannelName(channelName);
        setChannelOwnerUser(channelOwnerUser);

        this.channelUsers = new ArrayList<>();
        if (channelUsers != null) {
            this.channelUsers.addAll(channelUsers);
        }
    }

    public Channel(String channelName, User channelOwnerUser) {
        super();
        setChannelName(channelName);
        setChannelOwnerUser(channelOwnerUser);
        this.channelUsers = new ArrayList<>();
    }

    public void addUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("유저를 추가해주세요");
        }
        
        setUpdatedAt();
    }

    public void removeUser(User user) {
        channelUsers.stream()
                .filter(Objects::nonNull)
                .filter(u -> u.getUserId().equals(user.getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다."));

        channelUsers.remove(user);
        setUpdatedAt();
    }

    private void setChannelName(String channelName) {
        checkChannelName(channelName);
        this.channelName = channelName;
        setUpdatedAt();
    }

    private void setChannelOwnerUser(User channelOwnerUser) {
        checkChannelOwnerUser(channelOwnerUser);
        this.channelOwnerUser = channelOwnerUser;
        setUpdatedAt();
    }

    private void checkChannelName(String channelName) {
        if(channelName == null || channelName.isEmpty()) {
            throw new IllegalArgumentException("채널 이름을 작성해주세요.");
        }
    }

    private void checkChannelOwnerUser(User channelOwnerUser) {
        if(channelOwnerUser == null) {
            throw new IllegalAccessError("방장을 지정해주세요.");
        }
    }

    public String updateChannelName(String updateChannelName) {
        setChannelName(updateChannelName);
        return this.channelName;
    }

    public User updateOwnerUser(User updateOwnerUser) {
        setChannelOwnerUser(updateOwnerUser);
        return this.channelOwnerUser;
    }

    public List<User> updateChannelUsers(List<User> updateChannelUsers) {
        if (updateChannelUsers == null) {
            this.channelUsers = new ArrayList<>();
        } else {
            this.channelUsers = new ArrayList<>(updateChannelUsers);
        }
        setUpdatedAt();
        return this.channelUsers;
    }


    public UUID getChannelId() {
        return getId();
    }

    public String getChannelName() {
        return channelName;
    }

    public User getChannelOwnerUser() {
        return channelOwnerUser;
    }

    public List<User> getChannelUsers() {
        return channelUsers;
    }

    public Long getCreatedAt() {
        return getCreatedAtBaseObject();
    }

    public Long getUpdatedAt() {
        return getUpdatedAtBaseObject();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelOwnerUser=" + channelOwnerUser +
                ", channelUsers=" + channelUsers +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}

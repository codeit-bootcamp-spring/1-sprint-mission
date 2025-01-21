package com.srint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    private String channelName;
    private User channelOwner;
    List<User> joinedUsers;
    List<Message> messages;

    public Channel(String channelName, User channelOwner) {

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.channelName = channelName;
        this.channelOwner = channelOwner;
        joinedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getChannelName() {
        return channelName;
    }
    public User getChannelOwner() {
        return channelOwner;
    }

    public List<User> getJoinedUsers() {
        return joinedUsers;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        setUpdatedAt();
    }

    public void setChannelOwner(User channelOwner) {
        this.channelOwner = channelOwner;
        setUpdatedAt();
    }

    public void setJoinedUsers(User user){
        if(joinedUsers.contains(user)){
            throw new IllegalStateException("이미 채널에 존재하는 회원입니다.");
        }
        joinedUsers.add(user);
        user.getMyChannels().add(this);
        setUpdatedAt();
    }

    public void deleteMessage(Message message) {
        if(!messages.contains(message)){
            throw new IllegalStateException("채널에 존재하지 않는 메시지입니다.");
        }
        messages.remove(message);
    }

    public void deleteJoinedUser(User user){
        if(!joinedUsers.contains(user)){
            throw new IllegalStateException("가입하지 않은 채널입니다.");
        }
        joinedUsers.remove(user);
        //user.getMyChannels().remove(this);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelName='" + channelName + '\'' +
                ", channelOwner=" + channelOwner +"\n"+
                ", joinedUsers=" + joinedUsers +
                ", messages=" + messages +
                '}';
    }
}

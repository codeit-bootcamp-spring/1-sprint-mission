package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private long createdAt;
    private long updatedAt;
    private String title;
    private String description;
    private ChannelType channelType;
    private List<Message> messageList;
    private List<User> memberList;

    public Channel(ChannelType channelType, String title, String description) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.title = title;
        this.description = description;
        this.channelType = channelType;
        messageList = new ArrayList<>();
        memberList = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<User> getMemberList() {
        return memberList;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateTitle(String title) {
        this.title = title;
        updateUpdatedAt();
    }

    public void updateDescription(String description) {
        this.description = description;
        updateUpdatedAt();
    }

    public void addMember(User newMember) {
        memberList.add(newMember);
        updateUpdatedAt();
    }

    public void removeMember(User newMember) {
        memberList.remove(newMember);
        updateUpdatedAt();
    }

    public void addMessage(Message newMessage) {
        messageList.add(newMessage);
    }

    public void removeMessage(Message removeMessage) {
        messageList.remove(removeMessage);
    }

    @Override
    public String toString() {
        return "Channel{id:" + id + ",title:" + title +  ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
    }

}

package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String title;
    private User owner;
    private List<Message> messageList;
    private List<User> memberList;

    public Channel(String title, User owner) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.title = title;
        this.owner = owner;
        messageList = new ArrayList<>();
        memberList = new ArrayList<>();
        memberList.add(owner);
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

    public User getOwner() {
        return owner;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<User> getMemberList() {
        return memberList;
    }

    // update
    public void updateTitle() {
        this.title = title;
    }

    public void addMessage(Message newMessage) {
        messageList.add(newMessage);
    }

    public void removeMessage(Message removeMessage) {
        messageList.remove(removeMessage);
    }
}

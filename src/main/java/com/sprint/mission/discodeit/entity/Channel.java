package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final User owner;
    private final List<Message> messageList;
    private final List<User> memberList;
    private final long createdAt;
    private long updatedAt;
    private String title;

    public Channel(String title, User owner) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        updateTitle(title);
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

    private boolean setTitle(String title){
        if (title.isBlank()){
            return false;
        }
        this.title = title;
        return true;
    }

    // update
    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateTitle(String title) {
        if (setTitle(title)){
            updateUpdatedAt();
        } else {
            System.out.println("채널명을 입력해주세요.");
        }
    }

    public void addMember(User newMember) {
        memberList.add(newMember);
    }

    public void removeMember(User newMember) {
        memberList.remove(newMember);
    }

    public void addMessage(Message newMessage) {
        messageList.add(newMessage);
    }

    public void removeMessage(Message removeMessage) {
        messageList.remove(removeMessage);
    }

    public String displayInfoChannel() {
        return "Channel title: " + title + ", Chennel Owner: " + owner.getName() + ", Channel Member: " + memberList;
    }
}

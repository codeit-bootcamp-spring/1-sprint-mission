package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private String description;
    private Set<User> participants;
    private List<Message> messageList;

    public Channel(String name, String description, Set<User> participants, List<Message> messageList) {
        super();
        this.name = name;
        this.description = description;
        this.participants= participants;
        this.messageList = messageList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void updateParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void updateMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}

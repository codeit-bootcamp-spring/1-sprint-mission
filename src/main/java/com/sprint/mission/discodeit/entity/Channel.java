package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private String description;
    private Set<User> participants;
    private List<Message> messageList;
    private ChannelType channelType;

    public Channel(String name, String description, Set<User> participants, List<Message> messageList, ChannelType channelType) {
        super();
        this.name = name;
        this.description = description;
        this.participants= participants;
        this.messageList = messageList;
        this.channelType = channelType;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
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

    public ChannelType getChannelType() {
        return channelType;
    }

    public void updateChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }
    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", participants=[" + participants + "]" +
                ", messageCount=" + ((messageList != null) ? messageList.size() : 0) +
                '}';
    }
}

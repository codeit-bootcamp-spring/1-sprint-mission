package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private String description;
    private HashMap<UUID,User> participants;
    private List<Message> messageList;
    private ChannelType channelType;

    public Channel(String name, String description, HashMap<UUID,User> participants, List<Message> messageList, ChannelType channelType) {
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

    public HashMap<UUID,User> getParticipants() {
        return participants;
    }

    public void updateParticipants(HashMap<UUID,User> participants) {
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
                ", participants=[" + participants.values() + "]" +
                ", messageCount=" + ((messageList != null) ? messageList.size() : 0) +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return getId().equals(channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends BaseEntity {
    private String name;
    private String description;
    private Set<String> participantIds;
    private List<UUID> messageIds;

    public Channel(String name, String description, Set<String> participantIds, List<UUID> messageIds) {
        this.name = name;
        this.description = description;
        this.participantIds = participantIds;
        this.messageIds = messageIds;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<String> participantIds) {
        this.participantIds = participantIds;
    }

    public List<UUID> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<UUID> messageIds) {
        this.messageIds = messageIds;
    }
}

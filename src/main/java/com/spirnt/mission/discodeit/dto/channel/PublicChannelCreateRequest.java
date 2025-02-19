package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PublicChannelCreateRequest {
    private String name;
    private String description;
    private List<UUID> usersId;

    public PublicChannelCreateRequest(String name, String description, List<UUID> users) {
        this.name = name;
        this.description = description;
        this.usersId = users;
    }
}

package com.sprint.mission.discodeit.dto;

import lombok.Getter;

@Getter
public class ChannelDto {
    private String name;
    private String description;

    private ChannelDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ChannelDto of(String name, String description) {
        return new ChannelDto(name, description);
    }
}

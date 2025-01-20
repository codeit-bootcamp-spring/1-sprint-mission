package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ChannelReqDTO implements ChannelDTO {
    private User owner;
    private String serverName;
    private String description;
    private String iconImgPath;

    public ChannelReqDTO(User owner, String serverName, String description, String iconImgPath) {
        this.owner = owner;
        this.serverName = serverName;
        this.description = Objects.requireNonNullElse(description, "");
        this.iconImgPath = Objects.requireNonNullElse(iconImgPath, "defaultServerIconImg.png");
    }

    @Override
    public String getOwnerName() {
        return owner.getUserName().getName();
    }
}

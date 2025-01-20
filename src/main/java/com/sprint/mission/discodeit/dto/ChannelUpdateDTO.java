package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

@Getter
public class ChannelUpdateDTO implements ChannelDTO {
    private User owner;
    private String serverName;
    private String description;
    private String iconImgPath;

    public ChannelUpdateDTO(User owner, String serverName, String description, String iconImgPath) {
        this.owner = owner;
        this.serverName = serverName;
        this.description = description;
        this.iconImgPath = iconImgPath;
    }
    
    @Override
    public String getOwnerName() {
        return owner.getUserName().getName();
    }
}

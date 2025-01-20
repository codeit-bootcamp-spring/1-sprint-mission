package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

@Getter
public class ChannelResDTO implements ChannelDTO {
    private Long id;
    private String uuid;
    private String ownerName;
    private String serverName;
    private String description;
    private String iconImgPath;

    public ChannelResDTO(Long id, Channel channel, User owner) {
        this.id = id;
        this.uuid = channel.getId().toString();
        this.ownerName = owner.getUserName().getName();
        this.serverName = channel.getServerName().getName();
        this.description = channel.getDescription();
        this.iconImgPath = channel.getIconImgPath();
    }
}

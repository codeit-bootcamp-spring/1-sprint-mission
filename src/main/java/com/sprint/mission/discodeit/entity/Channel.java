package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;
    private String description;
    private ChannelType type; //채널 타입
    private List<UUID> userId; //channel이 가지고 있는 유저 리스트

    public Channel(ChannelDTO dto, ChannelType type){
        super();
        this.channelName = dto.getChannelName();
        this.description = dto.getDescription();
        this.type = type;
        if(dto.getUserId() != null) {
            this.userId = dto.getUserId();
        } else {
            this.userId = new ArrayList<>();
        }
    }

    public void update(ChannelDTO dto) {
        if(dto.getChannelName() != null && !dto.getChannelName().equals(this.channelName)) {
            this.channelName = dto.getChannelName();
        }
        if(dto.getDescription() != null && !dto.getDescription().equals(this.description)) {
            this.description = dto.getDescription();
        }
    }

    public boolean containUser(UUID userId) {
        return this.userId.contains(userId);
    }
}

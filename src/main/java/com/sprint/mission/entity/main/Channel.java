package com.sprint.mission.entity.main;

import com.sprint.mission.config.BaseTimeEntity;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString @Getter @Setter
@Schema(description = "채널")
public class Channel  extends BaseTimeEntity implements Serializable {

    @ToString.Exclude
    private static final long serialVersionUID = 2L;

    private UUID id;
    private ChannelType channelType;
    private String name;
    private String description;

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.channelType = channelType;
        this.description = description;
    }

    public void update(String newName, String newDescription) {
        this.name = newName;
        this.description = newDescription;
    }
}
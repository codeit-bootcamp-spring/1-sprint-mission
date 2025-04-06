package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter @Setter
@Entity
@SuperBuilder
@Table(name = "channels")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReadStatus> readStatuses = new ArrayList<>();

    public void update(String name, String description, ChannelType channelType) {
        this.name = name;
        this.description = description;
        this.type = channelType;
    }
}
package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter @Setter
@Entity @Builder
@Table(name = "channels")
@AllArgsConstructor
@NoArgsConstructor
public class Channel{

    @Id @GeneratedValue
    @Column(name = "channel_id")
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    public Channel(String name, String description, ChannelType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public void update(String name, String description, ChannelType channelType) {
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        this.type = channelType;
    }
}
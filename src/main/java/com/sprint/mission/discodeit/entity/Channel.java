package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "channels")
@RequiredArgsConstructor
public class Channel{

    @Id
    private String id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.description = description;
        this.type = channelType;
    }

    public void update(String name, String description, ChannelType channelType) {
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        this.type = channelType;
    }
    @ManyToMany
    @JoinTable(
            name = "channel_members",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    public void addMember(User user) {
        this.members.add(user);
    }
}
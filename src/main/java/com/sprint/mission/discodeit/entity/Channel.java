package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@Entity @Builder
@Table(name = "channels")
@AllArgsConstructor
@NoArgsConstructor
public class Channel{

    @Id @GeneratedValue
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;

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
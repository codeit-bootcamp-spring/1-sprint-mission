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
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        this.users.add(user);
    }
}
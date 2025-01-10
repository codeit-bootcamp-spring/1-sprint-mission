package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private List<User> members;
    private String name;
    private User creator;

    public Channel(List<User> members, String name, User creator) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.members = members;
        this.name = name;
        this.creator = creator;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public User getCreator() {
        return creator;
    }

    public void update(User user) {
        this.members.add(user);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeUser(User user) {
        members.remove(user);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createdAtFormatted = createdAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";
        String updatedAtFormatted = updatedAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";

        String creatorName = creator != null ? creator.getName() : "Unknown";

        String membersNames = (members != null && !members.isEmpty())
                ? members.stream().map(User::getName).collect(Collectors.joining(", "))
                : "No members";

        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                ", members=" + membersNames +
                ", name='" + name + '\'' +
                ", creator='" + creatorName + '\'' +
                '}';
    }

}

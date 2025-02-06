package com.sprint.mission.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final String firstId; // 콘솔창 용
    @Setter
    private String name;
    private String description;

    @Setter
    private Instant updatedAt;
    private final Instant createdAt;

    public Channel(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        String idAsString = id.toString();
        this.firstId = idAsString.split("-")[0];
        this.createdAt = Instant.now();
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(firstId, channel.firstId) && Objects.equals(name, channel.name)
                && Objects.equals(createdAt, channel.createdAt) && Objects.equals(updatedAt, channel.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstId, name, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return  "`[" + firstId + "]" +
                " 채널명: " + name +
                ", 유저 수: " +'`';
    }
}
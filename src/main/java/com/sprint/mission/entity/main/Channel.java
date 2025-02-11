package com.sprint.mission.entity.main;

import com.sprint.mission.service.dto.request.ChannelDtoForRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private ChannelType channelType;
    private String description;
    private String name;
    private Instant lastMessageTime;

    private final List<User> userList = new ArrayList<>();

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.channelType = channelType;
        this.description = description;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Channel createChannelByRequestDto(ChannelDtoForRequest dto) {
        return new Channel(dto.getName(), dto.getDescription(), dto.getChannelType());
    }

    // 양방향은 channel에서만
    public void addUser(User user) {
        //if (!userList.contains(user))
        userList.add(user);
        user.getChannels().add(this);
        user.changeReadStatus(id); // 흠 등록과 동시에 넣는게 흠
    }

    public void removeUser(User user) {
        //if (userList.contains(user))
        userList.remove(user);
        user.getChannels().remove(this);
    }

    public int countUser() {
        return userList.size();
    }

    public void updateLastMessageTime() {
        lastMessageTime = Instant.now();
    }
}
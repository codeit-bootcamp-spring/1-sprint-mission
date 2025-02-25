package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


@Getter
@Setter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = -5943403271027267293L;
    private UUID channelId = UUID.randomUUID();
    private String name;
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private final Map<UUID, String> users = new HashMap<>();
    private List<UUID> privateUsers = new ArrayList<>();
    private Boolean checkPrivateChannel;

    public void setUsers(UUID userId, String name){
        users.put(userId,name);
    }

    public void setPrivateUsers(UUID userId){
        privateUsers.add(userId);
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
    @Override
    public String toString() {
        return "Channel\n" +
                "channel id = " + channelId + '\n' +
                "name = " + name + '\n' +
                "createdAt = " + (createdAt) + '\n' +
                "updatedAt = " + (updatedAt) +'\n' +
                "users = " + users.values();
    }

}

package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    //직렬화..?
    private UUID id;
    private User user;
    private Channel channel;
    private Instant lastReadAt;


    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void updateRedaTime() {
        this.lastReadAt = Instant.now();
    }

}

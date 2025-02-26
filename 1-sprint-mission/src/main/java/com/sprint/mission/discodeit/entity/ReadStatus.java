package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
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

    public void updateReadTime(Instant newLastReadAt) {
        if(newLastReadAt.isAfter(this.lastReadAt)) {
            this.lastReadAt = newLastReadAt;
        }

    }

}

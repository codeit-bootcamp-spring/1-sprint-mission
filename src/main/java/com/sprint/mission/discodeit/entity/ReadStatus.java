package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


/**
 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.

 User 마다 여러 개의 ReadStatus를 가질 수 있다고 상정합니다.
 하나의 채널에 하나의 ReadStatus를 가집니다.

 ! ReadStatus lastMessageReadTime 시간 이후의 createdAt된 Messages 는 읽지 않은 메세지이다. !

 **/

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private UUID channelId;
    private Instant lastMessageReadAt; // 마지막으로 읽은 메시지의 시각

    public ReadStatus(UUID userId, UUID channelId){
        this.userId = userId;
        this.channelId = channelId;
        this.lastMessageReadAt = Instant.now();
    }

    public void refreshLastMessageReadTime(){
        this.lastMessageReadAt = Instant.now();
    }

}
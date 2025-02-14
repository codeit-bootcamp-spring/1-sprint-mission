package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class BinaryMessageContentDto {


    //private final UUID id;
    //private final Instant createdAt;

    private final byte[] bytes;
    private final UUID messageId;

    public BinaryMessageContentDto(BinaryMessageContent contents) {
        //this.id = contents.getId();
        //this.createdAt = contents.getCreatedAt();
        this.messageId = contents.getMessageId();
        this.bytes = contents.getBytes();
    }

    //
//    public BinaryContentDto BinaryUserContentDto(UUID userId, byte[] bytes){
//        BinaryContentDto bcd = new BinaryContentDto(bytes);
//        bcd.userId = userId;
//        bcd.bytes = bytes;
//        return bcd;
//    }
}

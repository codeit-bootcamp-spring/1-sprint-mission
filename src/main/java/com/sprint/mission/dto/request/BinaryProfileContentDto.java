package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.entity.addOn.BinaryProfileContent;
import com.sprint.mission.entity.main.User;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class BinaryProfileContentDto {


    private final UUID userId;
    private final Instant createdAt; // user생성기준??
    private final byte[] bytes;

    public BinaryProfileContentDto(BinaryProfileContent profileContent) {
        this.userId = profileContent.getUserId();
        this.createdAt = profileContent.getCreatedAt();
        this.bytes = profileContent.getBytes();
    }

    public BinaryProfileContentDto(User user, byte[] bytes) {
        this.userId = user.getId();
        this.createdAt = user.getCreateAt();
        this.bytes = bytes;
    }

    //
//    public BinaryContentDto BinaryUserContentDto(UUID userId, byte[] bytes){
//        BinaryContentDto bcd = new BinaryContentDto(bytes);
//        bcd.userId = userId;
//        bcd.bytes = bytes;
//        return bcd;
//    }
//
//    public BinaryContentDto BinaryMessageContentDto(UUID messageId, byte[] bytes){
//        BinaryContentDto bcd = new BinaryContentDto(bytes);
//        bcd.messageId = messageId;
//        bcd.bytes = bytes;
//        return bcd;
//    }

}

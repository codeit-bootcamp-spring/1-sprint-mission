package com.sprint.mission.service.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContentDto {

    private UUID id;
    private byte[] bytes;

    public BinaryContentDto(UUID id, byte[] bytes) {
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

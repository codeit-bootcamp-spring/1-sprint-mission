package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Message;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

import java.util.List;


@Schema(description = "메시지 조회 응답 DTO")
public record FindMessageDto (
    UUID writerId,
    UUID channelId,
    List<UUID> attachmentIdList,
    String content) {

    public FindMessageDto(Message message) {
        this(
            message.getWriterId(),
            message.getChannelId(),
            message.getAttachmentIdList(),
            message.getContent());
    }

    //    public static FindMessageDto toDto(Message message) {
//        return new FindMessageDto(
//            message.getWriterId(),
//            message.getChannelId(),
//            message.getAttachmentIdList(),
//            message.getContent());
//    }
}

package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;

public record MessageResponseDto(
        //객체 식별 id
        String id,
        //생성 날짜
        Instant createdAt,
        //수정 시간
        Instant updatedAt,
        //메세지 작성자
        String senderId,
        //메세지 내용
        String content,
        //메세지가 생성된 채널
        String channelId
) {
    public static MessageResponseDto from(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getSenderId(),
                message.getContent(),
                message.getChannelId()
        );
    }
    @Override
    public String toString(){
        return "[MessageResponseDto] "+
                "{id:" +id
                +" senderId:" +senderId
                +" content:" +content
                +" channelId:" +channelId
                +" createdAt:" +createdAt
                +" updatedAt:" +updatedAt;
    }
}

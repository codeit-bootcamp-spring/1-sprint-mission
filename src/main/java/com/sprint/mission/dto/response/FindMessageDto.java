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

}

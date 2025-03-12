//package com.sprint.mission.dto.response;
//
//import com.sprint.mission.entity.main.ChannelType;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//@Schema(description = "채널 조회 응답 DTO")
//public record FindChannelAllDto(
//    UUID id, ChannelType channelType,
//    String name, String description,
//    List<UUID> participantIds,
//    Instant lastMessageAt) {
//}

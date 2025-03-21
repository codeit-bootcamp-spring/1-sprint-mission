package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "채널 조회 응답 DTO")
public record ChannelDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description,
        List<UserDto> participants,
        Instant lastMessageAt) {
}
// private
//[ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
//[ ] name과 description 속성은 생략합니다.

//findAll
//DTO를 활용하여:
//[ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
//[ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
//[ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId
//[ ] PUBLIC 채널 목록은 전체 조회합니다.
//[ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.


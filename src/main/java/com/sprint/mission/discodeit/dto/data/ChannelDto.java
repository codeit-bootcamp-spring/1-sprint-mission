package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type, // TODO찐: API스펙대로 String 형으로 수정해야하나..?
    String name,
    String description,
    List<UserDto> participants, // TODO찐: array<Object> 형으로 수정
    Instant lastMessageAt // TODO찐: date-time으로 수정
) {

}

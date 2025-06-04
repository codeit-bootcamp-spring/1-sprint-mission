package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelDto(
	UUID id,
	ChannelType type,
	String name,
	String description,
	List<UserDto> participants,
	Instant lastMessageAt
) {

}

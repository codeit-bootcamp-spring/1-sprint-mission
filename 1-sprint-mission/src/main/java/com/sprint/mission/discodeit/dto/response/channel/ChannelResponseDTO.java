package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDTO(UUID channelId, String name, String description, ChannelType type, List<UUID>memberIds, Instant lastMessageAt)//최근 메시지 시간
{}

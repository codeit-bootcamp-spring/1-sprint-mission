package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record MessageResponseDto(UUID id, Instant createdAt, Instant updatedAt, String content, UUID channelId,
                                 UserResponseDto author, List<BinaryContentResponseDto> attachments) {
}

package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

@Builder
public record ChannelUpdateDto(String name, String description) {
}

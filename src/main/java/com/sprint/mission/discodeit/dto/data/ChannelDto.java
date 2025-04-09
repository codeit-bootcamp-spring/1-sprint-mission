package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    UUID id;
    Channel.ChannelType type;
    String name;
    String description;
    List<UserDto> participants; // TODO찐: array<Object> 형으로 수정
    Instant lastMessageAt; // TODO찐: date-time으로 수정
}

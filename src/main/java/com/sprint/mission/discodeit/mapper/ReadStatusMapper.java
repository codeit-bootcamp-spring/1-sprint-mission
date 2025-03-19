package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusMapper {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponseDto toResponseDto(ReadStatus readStatus) {
        return ReadStatusResponseDto.builder()
                .id(readStatus.getId())
                .userId(readStatus.getUser().getId())
                .channelId(readStatus.getChannel().getId())
                .lastReadAt(readStatus.getLastReadAt())
                .build();
    }

    public ReadStatus toEntity(ReadStatusRequestDto readStatusRequestDto) {
        User user = userRepository.findById(readStatusRequestDto.userId()).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        Channel channel = channelRepository.findById(readStatusRequestDto.channelId()).orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
        return new ReadStatus(user, channel, readStatusRequestDto.lastReadAt());
    }
}

package com.sprint.mission.discodeit.application.service.readstatus;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.readstatus.interfaces.ReadStatusRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public ReadStatusService(
            ReadStatusRepository readStatusRepository,
            UserService userService,
            ChannelService channelService
    ) {
        this.readStatusRepository = readStatusRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    public void updateLastReadTime(UUID userId, ReadStatusUpdateRequestDto requestDto) {
        User foundUser = userService.findOneByIdOrThrow(userId);
        Channel foundChannel = channelService.findOneByIdOrThrow(requestDto.channelId());
        ReadStatus readStatus = findOneByUserIdAndChannelId(foundUser, foundChannel).orElseGet(() -> new ReadStatus(foundUser, foundChannel));
        readStatus.updateLastReadAt(Instant.now());
        readStatusRepository.save(readStatus);
    }

    public Optional<ReadStatus> findOneByUserIdAndChannelId(User foundUser, Channel foundChannel) {
        return readStatusRepository.findOneByUserIdAndChannelId(foundUser, foundChannel);
    }
}

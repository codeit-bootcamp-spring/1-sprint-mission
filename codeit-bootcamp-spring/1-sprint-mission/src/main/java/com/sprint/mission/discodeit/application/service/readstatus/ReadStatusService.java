package com.sprint.mission.discodeit.application.service.readstatus;

import com.sprint.mission.discodeit.application.service.interfaces.ChannelService;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.exception.EntityNotFoundException;
import com.sprint.mission.discodeit.repository.readstatus.interfaces.ReadStatusRepository;
import java.time.Instant;

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

    public void updateLastReadTime(User user, Channel channel) {
        ReadStatus readStatus = readStatusRepository.findOneByUserIdAndChannelId(user, channel)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        readStatus.updateLastReadAt(Instant.now());
        readStatusRepository.save(readStatus);
    }
}

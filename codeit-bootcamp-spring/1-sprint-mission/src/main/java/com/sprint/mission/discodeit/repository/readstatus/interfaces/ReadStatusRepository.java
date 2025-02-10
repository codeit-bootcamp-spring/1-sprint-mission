package com.sprint.mission.discodeit.repository.readstatus.interfaces;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import java.util.Optional;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findOneByUserIdAndChannelId(User user, Channel channel);
}

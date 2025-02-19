package com.sprint.mission.discodeit.repository.channel.interfaces;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

    Channel save(Channel channel);

    Optional<Channel> findOneById(UUID channelId);

    List<Channel> findAllByUserId(UUID userId);

    void deleteById(UUID uuid);

    boolean isExistUser(User user, Channel channel);
}

package com.sprint.mission.discodeit.repository.readstatus;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.readStatus.ReadStatus;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.readstatus.interfaces.ReadStatusRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ReadStatusInMemoryRepository implements ReadStatusRepository {

    private final Map<ReadStatusKey, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        ReadStatusKey readStatusKey = new ReadStatusKey(readStatus.getUser(), readStatus.getChannel());
        readStatusMap.put(readStatusKey, readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findOneByUserIdAndChannelId(User user, Channel channel) {
        ReadStatusKey readStatusKey = new ReadStatusKey(user, channel);
        return Optional.ofNullable(readStatusMap.get(readStatusKey));
    }

    private static class ReadStatusKey {
        private final UUID userId;
        private final UUID channelId;

        public ReadStatusKey(User user, Channel channel) {
            this.userId = user.getId();
            this.channelId = channel.getId();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ReadStatusKey that = (ReadStatusKey) o;
            return Objects.equals(userId, that.userId) && Objects.equals(channelId, that.channelId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, channelId);
        }
    }
}

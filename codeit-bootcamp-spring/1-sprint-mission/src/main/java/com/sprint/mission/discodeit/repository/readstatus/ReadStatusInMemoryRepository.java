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
import org.springframework.stereotype.Repository;

@Repository
public class ReadStatusInMemoryRepository implements ReadStatusRepository {

    private final Map<ReadStatusKey, ReadStatus> readStatusStore = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        ReadStatusKey readStatusKey = ReadStatusKey.of(readStatus.getUser(), readStatus.getChannel());
        readStatusStore.put(readStatusKey, readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findOneByUserIdAndChannelId(User user, Channel channel) {
        ReadStatusKey readStatusKey = ReadStatusKey.of(user, channel);
        return Optional.ofNullable(readStatusStore.get(readStatusKey));
    }

    @Override
    public void deleteByChannel(Channel channel) {
        channel.getParticipantUserId().stream()
                .map(userId -> new ReadStatusKey(userId, channel.getId()))
                .forEach(readStatusStore::remove);
    }

    private static class ReadStatusKey {
        private final UUID userId;
        private final UUID channelId;

        public ReadStatusKey(UUID userId, UUID channelId) {
            this.userId = userId;
            this.channelId = channelId;
        }

        public static ReadStatusKey of(User user, Channel channel) {
            return new ReadStatusKey(user.getId(), channel.getId());
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

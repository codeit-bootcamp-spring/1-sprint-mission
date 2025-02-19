package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();
    private final Map<UUID, List<UUID>> channelToUsers = new HashMap<>();
    private final Map<UUID, List<UUID>> userToReadStatus = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);

        channelToUsers.computeIfAbsent(readStatus.getChannel().getId(), k -> new ArrayList<>())
                .add(readStatus.getUser().getId());

        userToReadStatus.computeIfAbsent(readStatus.getUser().getId(), k -> new ArrayList<>())
                .add(readStatus.getId());
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        return channelToUsers.getOrDefault(channelId, Collections.emptyList());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> userIds = channelToUsers.remove(channelId);
        if (userIds != null) {
            userIds.forEach(userId -> {
                List<UUID> readStatusList = userToReadStatus.get(userId);
                if (readStatusList != null) {
                    readStatusList.removeIf(readStatusId -> {
                        ReadStatus readStatus = data.get(readStatusId);
                        return readStatus != null && readStatus.getChannel().getId().equals(channelId);
                    });
                }
            });
        }
        data.entrySet().removeIf(entry -> entry.getValue().getChannel().getId().equals(channelId));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return userToReadStatus.getOrDefault(userId, Collections.emptyList()).stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserAndChannel(User user, Channel channel) {
        return data.values().stream()
                .anyMatch(readStatus -> readStatus.getUser().getId().equals(user.getId()) &&
                        readStatus.getChannel().getId().equals(channel.getId()));
    }

    @Override
    public void deleteById(UUID id) {
        ReadStatus readStatus = data.remove(id);
        if (readStatus != null) {
            channelToUsers.getOrDefault(readStatus.getChannel().getId(), new ArrayList<>())
                    .remove(readStatus.getUser().getId());
            userToReadStatus.getOrDefault(readStatus.getUser().getId(), new ArrayList<>())
                    .remove(id);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
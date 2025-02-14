package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusStorage = new ConcurrentHashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusStorage.put(readStatus.getId(), readStatus);
    }

    @Override
    public List<ReadStatus> findAllByChannel(Channel channel) {
        return readStatusStorage.values().stream()
                .filter(readStatus -> channel.equals(readStatus.getChannel()))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUser(User user) {
        return readStatusStorage.values().stream()
                .filter(readStatus -> user.equals(readStatus.getUser()))
                .toList();
    }

    @Override
    public List<Channel> findChannelsByUser(User owner) {
        return readStatusStorage.values().stream()
                .filter(readStatus -> owner.equals(readStatus.getUser()))
                .map(ReadStatus::getChannel)
                .distinct() //중복 제거
                .toList();
    }

    @Override
    public List<UUID> findUserIdsByChannel(Channel channel) {
        return readStatusStorage.values().stream()
                .filter(readStatus -> channel.equals(readStatus.getChannel()))
                .map(readStatus -> readStatus.getUser().getId())
                .distinct() // 중복제거
                .toList();
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusStorage.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserAndChannel(User user, Channel channel) {
        return readStatusStorage.values().stream()
                .filter(readStatus -> user.equals(readStatus.getUser()) && channel.equals(readStatus.getChannel()))
                .findFirst();
    }

    @Override
    public boolean existsByUserAndChannel(User user, Channel channel) {
        return readStatusStorage.values().stream()
                .anyMatch(readStatus -> user.equals(readStatus.getUser()) && channel.equals(readStatus.getChannel()));
    }

    @Override
    public void deleteByChannel(Channel channel) {
        readStatusStorage.values()
                .removeIf(readStatus -> channel.equals(readStatus.getChannel()));
    }

    @Override
    public void deleteById(UUID id) {
        readStatusStorage.remove(id);
    }
}

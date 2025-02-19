package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Profile("jcf")
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> repository;

    public JCFReadStatusRepository() {
        this.repository = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        repository.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByChannelId(UUID channelID) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelID))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ReadStatus readStatus) {
        repository.remove(readStatus.getId());
    }
}

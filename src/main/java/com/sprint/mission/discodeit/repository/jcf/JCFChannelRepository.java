package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enums.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<Long, Channel> data;
    private final AtomicLong idGenerator = new AtomicLong(1);   // ID 초기값 1

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Long save(Channel channel) {
        Long id = idGenerator.getAndIncrement();
        data.put(id, channel);
        return id;
    }

    @Override
    public Channel load(Long id) {
        return data.get(id);
    }

    @Override
    public Map.Entry<Long, Channel> load(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    @Override
    public Map<Long, Channel> loadAll() {
        return data;
    }

    @Override
    public void delete(Long id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("삭제할 채널이 존재하지 않습니다.");
        }
    }

    @Override
    public void update(Long id, Channel channel) {
        data.put(id, channel);
    }

    @Override
    public Map<Long, Channel> findChannelsByUserId(UUID userId) {
        return loadAll().entrySet().stream()
                .filter(channel ->
                        channel.getValue().getChannelType() == ChannelType.PUBLIC ||
                                channel.getValue().getOwnerId().equals(userId) ||   // Public은 전체 조회
                                channel.getValue().getMembers().stream()
                                        .anyMatch(uuid -> uuid.equals(userId))    // Private의 경우 필터링
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}

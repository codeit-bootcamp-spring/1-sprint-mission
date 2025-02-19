package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonReadStatusRepository extends JsonRepository<UUID, ReadStatus> implements ReadStatusRepository {
    public JsonReadStatusRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "readStatus.json",
                new TypeReference<HashMap<UUID, ReadStatus>>() {}
        );
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        if (readStatus == null) {
            throw new IllegalArgumentException("readStatus가 null입니다.");
        }

        map.put(readStatus.getChannelId(), readStatus);

        saveToJson();

        return map.get(readStatus.getChannelId());
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return map.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 채널 아이디로 찾기
     */
    @Override
    public ReadStatus findByChannelId(UUID channelId) {
        return map.get(channelId);
    }

    @Override
    public Map<UUID, ReadStatus> findAll() {
        if(map.isEmpty()) {
            throw new IllegalArgumentException("readStatus가 비어있음");
        }

        return map;
    }

    @Override
    public void remove(UUID channelId) {
        map.remove(channelId);
        saveToJson();
    }
}

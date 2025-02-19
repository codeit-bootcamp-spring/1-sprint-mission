package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonChannelRepository extends JsonRepository<UUID, Channel> implements ChannelRepository {
    public JsonChannelRepository(RepositoryProperties properties) {
        super(
                new ObjectMapper().registerModule(new JavaTimeModule()),
                properties,
                "channels.json",
                new TypeReference<HashMap<UUID, Channel>>() {}
        );
    }

    @Override
    public Channel saveChannel(Channel channel) {
        map.put(channel.getId(), channel);
        saveToJson();
        return channel;
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        loadFromJson();
        return map.get(channelId);
    }

    @Override
    public Map<UUID, Channel> findAllChannel() {
        loadFromJson();
        return map;
    }

    @Override
    public void removeChannelById(UUID channelId) {
        map.remove(channelId);
        saveToJson();
    }
}

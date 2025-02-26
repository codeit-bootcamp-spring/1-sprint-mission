package com.sprint.mission.discodeit.repository.file.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.RepositoryProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class JsonChannelRepository extends JsonRepository<UUID, Channel> implements
    ChannelRepository {

  public JsonChannelRepository(RepositoryProperties properties) {
    super(new ObjectMapper()
            .registerModule(new JavaTimeModule())  // Java 8 date/time 지원 모듈 추가
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true),
        properties,
        "channels.json",
        new TypeReference<HashMap<UUID, Channel>>() {
        });
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
  public Channel findChannelByName(String channelName) {
    return map.values().stream()
        .filter(ch -> ch.getChannelName().equals(channelName))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Map<UUID, Channel> findAllChannel() {
    loadFromJson();
    return map;
  }

  @Override
  public List<Channel> findAllChannelById(UUID channelId) {
    return map.values().stream()
        .filter(cha -> cha.getId().equals(channelId))
        .toList();
  }

  @Override
  public void removeChannelById(UUID channelId) {
    map.remove(channelId);
    saveToJson();
  }

  @Override
  public void clearData() {
    super.clearData();
  }

  @Override
  public void resetData() {
    super.resetData();
  }
}

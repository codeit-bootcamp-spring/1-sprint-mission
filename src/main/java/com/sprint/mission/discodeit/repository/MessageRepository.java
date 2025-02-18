package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);
    Message findById(UUID id);
    Map<UUID, Message> load();
    void delete(UUID id);

    void deleteByChannelId(UUID id);

    List<Message> findByChannelId(UUID channelId); //채널에 해당하는 메시지 리스트 반환
}

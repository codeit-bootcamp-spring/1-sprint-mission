package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.data.MessageData;

import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final MessageData messageData = MessageData.getInstance();

    // 데이터 저장
    @Override
    public void save(Message message) {

        messageData.save(message);
    }

    // 데이터 가져오기
    @Override
    public Map<UUID, Message> load() {

        return messageData.load();
    }

    // 데이터 삭제
    @Override
    public void delete(UUID id) {

        messageData.delete(id);
    }
}
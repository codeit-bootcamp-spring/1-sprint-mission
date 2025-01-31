package com.sprint.mission.discodeit.collection;

import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public class Messages {
    private final Map<UUID, Message> messages = new HashMap<>();

    // 메시지 추가
    public Optional<Message> add(UUID id, Message message) {
        messages.put(id, message);
        return Optional.of(message);
    }

    // 메시지 삭제
    public Optional<Message> remove(UUID id) {
        return Optional.ofNullable(messages.remove(id));
    }

    // 메시지 수정
    public Optional<Message> update(UUID id, String newText) {
        Message message = messages.get(id);
        if (message != null) {
            message.updateText(newText);
            return Optional.of(message);
        }
        return Optional.empty();
    }

    // 메시지 단건 조회
    public Optional<Message> get(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    // 메시지 개수 조회
    public int size() {
        return messages.size();
    }

    // 읽기 전용 맵 반환
    public Map<UUID, Message> asReadOnly() {
        return Collections.unmodifiableMap(messages);
    }
}

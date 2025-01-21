package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

/**
 * JCF 기반의 MessageRepository 구현체. 데이터를 메모리에 저장합니다.
 */
public class JCFMessageRepository implements MessageRepository {
    private final Map<String, Message> data = new HashMap<>();

    @Override
    public void saveAll(List<Message> messages) {
        messages.forEach(message -> data.put(message.getId().toString(), message));
    }

    @Override
    public List<Message> loadAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void reset() {
        data.clear(); // 메모리 데이터 초기화
        System.out.println("메모리 데이터가 초기화되었습니다.");
    }
}

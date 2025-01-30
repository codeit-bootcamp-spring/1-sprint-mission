package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private static final HashMap<UUID, Message> messagesMap = new HashMap<UUID, Message>();

    // 외부에서 생성자 접근 불가
    private JCFMessageRepository() {}

    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class JCFMessageRepositoryHolder {
        private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
    }

    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static JCFMessageRepository getInstance() {
        return JCFMessageRepositoryHolder.INSTANCE;
    }

    // I/O로 생성된 모든 메세지 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Message> getMessagesMap() {
        return messagesMap;
    }

    // 특정 메세지 객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Message getMessage(UUID messageId) {
        if (messagesMap.containsKey(messageId) == false) {
            return null;
        }
        return messagesMap.get(messageId);
    }

    // 특정 메세지 객체 여부 확인 후 삭제. 불값 반환
    @Override
    public boolean deleteMessage(UUID Id) {
        if (messagesMap.containsKey(Id) == false) {
            return false;
        }
        messagesMap.remove(Id);
        return true;
    }

    // 전달받은 메세지 객체 null 여부 확인 후 메세지 해쉬맵에 추가.
    @Override
    public boolean addMessage(Message message) {
        if (message == null) {
            return false;
        }
        messagesMap.put(message.getId(), message);
        return true;
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID messageId) {
        if (messagesMap.containsKey(messageId) == false) {
            return false;
        }
        return true;
    }

    // 전달받은 메세지맵 null 여부 확인 후 기존 메세지맵에 추가.
    public boolean addMessages(HashMap<UUID, Message> messages) {
        if (messages == null) {
            return false;
        }
        messagesMap.putAll(messages);
        return true;
    }
}

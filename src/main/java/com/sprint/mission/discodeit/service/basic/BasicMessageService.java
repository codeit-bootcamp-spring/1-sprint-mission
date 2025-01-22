package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

/**
 * BasicMessageService는 MessageService 인터페이스의 기본 구현체입니다.
 * 저장 로직은 MessageRepository 인터페이스를 통해 처리합니다.
 */
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    /**
     * BasicMessageService 생성자.
     *
     * @param messageRepository MessageRepository 구현체
     */
    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(Message message) {
        List<Message> messages = messageRepository.loadAll(); // 저장소에서 모든 메시지 로드
        messages.add(message); // 새로운 메시지 추가
        messageRepository.saveAll(messages); // 저장소에 저장
        System.out.println("메시지가 생성되었습니다: " + message);
    }

    @Override
    public Message readMessage(String id) {
        List<Message> messages = messageRepository.loadAll(); // 저장소에서 모든 메시지 로드
        return messages.stream()
                .filter(message -> message.getId().toString().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> readAllMessages() {
        return messageRepository.loadAll(); // 저장소에서 모든 메시지 로드
    }

    @Override
    public void updateMessage(Message message) {
        List<Message> messages = messageRepository.loadAll(); // 저장소에서 모든 메시지 로드
        boolean updated = false;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(message.getId())) {
                messages.set(i, message); // 메시지 업데이트
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다: " + message.getId());
        }
        messageRepository.saveAll(messages); // 저장소에 저장
        System.out.println("메시지가 업데이트되었습니다: " + message);
    }

    @Override
    public void deleteMessage(String id) {
        List<Message> messages = messageRepository.loadAll(); // 저장소에서 모든 메시지 로드
        if (!messages.removeIf(message -> message.getId().toString().equals(id))) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다: " + id);
        }
        messageRepository.saveAll(messages); // 저장소에 저장
        System.out.println("메시지가 삭제되었습니다: " + id);
    }
}

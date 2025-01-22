package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;


public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public BasicMessageService(MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }


    @Override
    public void createMessage(Message message) {
        validateMessage(message); // 유효성 검사
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
        validateMessage(message);

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

    private void validateMessage(Message message) {
        // 채널 유효성 검증
        Channel channel = channelService.readChannel(message.getChannel());
        if (channel == null) {
            throw new IllegalArgumentException("유효하지 않은 채널 ID입니다: " + message.getChannel());
        }

        // 사용자 유효성 검증
        User user = userService.readUser(message.getAuthor());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + message.getAuthor());
        }
    }
}

package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("fileMessageService")
@RequiredArgsConstructor
public class FileMessageService implements MessageService {

    private final @Qualifier("fileMessageRepository") MessageRepository messageRepository;

    @Override
    public MessageResponse create(MessageCreateRequest messageCreateRequest) {
        // 수정: getSenderId() → getAuthorId()
        Message message = new Message(
                UUID.randomUUID(),
                messageCreateRequest.getContent(),
                messageCreateRequest.getAuthorId(),
                messageCreateRequest.getChannelId(),
                Instant.now()
        );
        messageRepository.save(message);
        // 수정: 엔티티 기반 생성자를 사용하여 응답 객체 생성
        return new MessageResponse(message);
    }

    @Override
    public void update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setContent(messageUpdateRequest.getContent());
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageResponse> readAllByChannel(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(message -> new MessageResponse(message)) // 엔티티 기반 생성자 사용
                .toList();
    }

    @Override
    public List<MessageResponse> readAll() {
        return messageRepository.findAll().stream()
                .map(message -> new MessageResponse(message)) // 엔티티 기반 생성자 사용
                .toList();
    }
}

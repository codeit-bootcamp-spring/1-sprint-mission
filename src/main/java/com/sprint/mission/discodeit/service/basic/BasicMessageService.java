package com.sprint.mission.discodeit.service.basic;



import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService{
    private final MessageRepository repository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageRequest messageRequest) {
        Message message;

        boolean isPrivateMessage = messageRequest.recipientId() != null;
        boolean isChannelMessage = messageRequest.channelId() != null;

        if (isPrivateMessage) {
            // 1:1 메시지 생성
            message = new Message(
                    messageRequest.content(),
                    messageRequest.senderId(),
                    messageRequest.recipientId(),
                    null
            );
        } else if (isChannelMessage) {
            // 채널 메시지 생성
            message = new Message(
                    messageRequest.content(),
                    messageRequest.senderId(),
                    null,
                    messageRequest.channelId()
            );
        } else {
            throw new IllegalArgumentException("Either recipientId or channelId must be provided.");
        }

        repository.save(message);

        return MessageResponse.fromEntity(message);
    }

    @Override
    public MessageResponse readOne(UUID id) {
        return MessageResponse.fromEntity(repository.findById(id));
    }

    @Override
    public List<MessageResponse> readAll() {
        List<Message> messages = repository.readAll();
        List<MessageResponse> responses = messages.stream()
                            .map(message -> MessageResponse.fromEntity(message))
                            .collect(Collectors.toList());
        return responses;
    }


    @Override
    public List<MessageResponse> channelMessageReadAll(UUID channelId) {
        List<Message> messages = repository.readAll();
        List<MessageResponse> responses = messages.stream()
                .filter(message -> message.getChannelId() !=null && message.getChannelId().equals(channelId))
                .map(message -> MessageResponse.fromEntity(message))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public MessageResponse update(UUID id, MessageRequest messageRequest) {
        if (messageRequest.recipientId() != null){
            Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), messageRequest.recipientId(), null);
        }

        Message modifiMessage = new Message(messageRequest.content(), messageRequest.senderId(), null, messageRequest.channelId());

        Message message = repository.modify(id, modifiMessage);
        return MessageResponse.fromEntity(message);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }

}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;

    @Override
    public MessageResponse createMessage(CreateMessageRequest request) {
        return channelService.getChannel(request.channelID())
                .map(channel -> {
                    Message newMessage = new Message(request.text(), request.authorID(), request.channelID());
                    messageRepository.save(newMessage);
                    return new MessageResponse(newMessage.getId(), newMessage.getText(), newMessage.getAuthorId(), newMessage.getChannelId());
                }).orElse(null);
    }

    @Override
    public List<MessageResponse> getMessages() {
        return messageRepository.getAllMessages().stream()
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .toList();
    }

    @Override
    public List<MessageResponse> getMessagesByChannel(UUID ChannelID) {
        return channelService.getMessagesUUIDFromChannel(ChannelID).stream()
                .map(messageRepository::getMessageById)
                .filter(Objects::nonNull)
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MessageResponse> getMessage(UUID uuid) {
        return Optional.ofNullable(messageRepository.getMessageById(uuid))
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public Optional<MessageResponse> updateMessage(UpdateMessageRequest request) {
        return Optional.ofNullable(messageRepository.getMessageById(request.uuid()))
                .map(message -> {
                    message.updateText(request.text());
                    return messageRepository.save(message);
                })
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public void deleteMessage(UUID uuid) {
        messageRepository.deleteById(uuid);
    }
}

package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.collection.Messages;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JCFMessageService implements MessageService {
    private final Messages messages = new Messages();
    private final ChannelService channelService;

    @Override
    public MessageResponse createMessage(CreateMessageRequest request) {
        return channelService.getChannel(request.channelID())
                .map(channel -> {
                    Message newMessage = new Message(request.text(), request.authorID(), request.channelID());
                    messages.add(newMessage.getId(), newMessage);
                    channelService.addMessageToChannel(request.channelID(), newMessage.getId());
                    return new MessageResponse(newMessage.getId(), newMessage.getText(), newMessage.getAuthorId(), newMessage.getChannelId());
                })
                .get();
    }

    @Override
    public List<MessageResponse> getMessages() {
        return messages.getMessagesList().stream()
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .toList();
    }

    @Override
    public List<MessageResponse> getMessagesByChannel(UUID ChannelID) {
        List<UUID> messageUUIDs = channelService.getMessagesUUIDFromChannel(ChannelID);
        return messageUUIDs.stream()
                .map(messages::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .toList();
    }

    @Override
    public Optional<MessageResponse> getMessage(UUID uuid) {
        return messages.get(uuid)
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public Optional<MessageResponse> updateMessage(UpdateMessageRequest request) {
        return messages.update(request.uuid(), request.text())
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public void deleteMessage(UUID uuid) {
        messages.remove(uuid);
    }
}

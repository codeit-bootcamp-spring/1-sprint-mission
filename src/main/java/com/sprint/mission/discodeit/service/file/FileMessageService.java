package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "feature.service", havingValue = "file")
public class FileMessageService implements MessageService {
    private final FileMessageRepository repository;
    private final ChannelService channelService;


    @Override
    public synchronized MessageResponse createMessage(CreateMessageRequest request) {
        Optional<Message> response = channelService.getChannel(request.channelID())
                .map(channel -> {
                    Message newMessage = new Message(request.text(), request.authorID(), request.channelID());
                    repository.save(newMessage);
                    return newMessage;
                });
        channelService.addMessageToChannel(request.channelID(), response.get().getId());
        return response.map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId())).orElse(null);
    }

    @Override
    public List<MessageResponse> getMessages() {
        return repository.getAllMessages().stream()
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .toList();
    }

    @Override
    public List<MessageResponse> getMessagesByChannel(UUID channelID) {
        List<UUID> messageUUIDs = channelService.getMessagesUUIDFromChannel(channelID);
        return messageUUIDs.stream()
                .map(repository::getMessageById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()))
                .toList();
    }

    @Override
    public Optional<MessageResponse> getMessage(UUID uuid) {
        return repository.getMessageById(uuid)
                .map(message -> new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public synchronized Optional<MessageResponse> updateMessage(UpdateMessageRequest request) {
        Optional<Message> messageOpt = repository.getMessageById(request.uuid());
        if (messageOpt.isEmpty()) return Optional.empty();

        Message message = messageOpt.get();
        message.updateText(request.text());
        repository.save(message);
        return Optional.of(new MessageResponse(message.getId(), message.getText(), message.getAuthorId(), message.getChannelId()));
    }

    @Override
    public synchronized void deleteMessage(UUID uuid) {
        repository.deleteById(uuid);
    }
}

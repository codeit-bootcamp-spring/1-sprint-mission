package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final FileManager fileManager;

    @Override
    public Message createMessage(MessageRequest messageRequest) {
        Channel channel = channelRepository.findById(messageRequest.channelId())
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + messageRequest.channelId()));
        User user = channel.getUser(messageRequest.writer());
        Message message = Message.of(user, messageRequest.content(), channel);
        return messageRepository.save(message);
    }

    @Override
    public Message readMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
    }

    @Override
    public List<MessageDetailResponse> readAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId).stream()
                .map(MessageDetailResponse::from)
                .toList();
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
        message.updateContent(content);
        messageRepository.updateMessage(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);
        for (BinaryContent content : contents) {
            binaryContentRepository.delete(content.getId());
            fileManager.deleteFile(Path.of(content.getPath()));
        }
        messageRepository.deleteMessage(messageId);
    }
}
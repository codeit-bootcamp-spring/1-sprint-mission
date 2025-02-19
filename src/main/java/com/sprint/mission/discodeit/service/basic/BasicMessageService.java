package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        User sender = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NoSuchElementException("Sender not found"));

        Message message = new Message(request.getContent(), request.getChannelId(), request.getAuthorId());
        message = messageRepository.save(message);

        List<UUID> attachmentIds = saveAttachments(message, request.getAttachments());
        return new MessageResponse(message, sender.getUsername(), attachmentIds);

    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);

        return messages.stream().map(message -> {
            List<UUID> attachments = binaryContentRepository.findByMessage(message).stream()
                    .map(BinaryContent::getId)
                    .collect(Collectors.toList());

            return new MessageResponse(message, userRepository.findById(message.getAuthorId())
                    .map(User::getUsername).orElse("Unknown"), attachments);
        }).collect(Collectors.toList());
    }

    @Override
    public MessageResponse update(MessageUpdateRequest request) {
        Message message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        message.update(request.getContent());
        message = messageRepository.save(message);

        return new MessageResponse(message, userRepository.findById(message.getAuthorId())
                .map(User::getUsername).orElse("Unknown"), null);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        binaryContentRepository.deleteByMessageId(message);

        messageRepository.delete(message);
    }

    private List<UUID> saveAttachments(Message message, List<MultipartFile> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> attachmentIds = new ArrayList<>();
        for (MultipartFile file : attachments) {
            try {
                BinaryContent binaryContent = new BinaryContent(
                        userRepository.findById(message.getAuthorId()).orElseThrow(),
                        message,
                        file.getBytes(),
                        file.getContentType()
                );
                binaryContent = binaryContentRepository.save(binaryContent);
                attachmentIds.add(binaryContent.getId());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save attachment", e);
            }
        }
        return attachmentIds;
    }
}

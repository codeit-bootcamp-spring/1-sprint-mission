package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

        if (!userRepository.existsId(messageCreateRequest.writerId())) {
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }

        boolean isMember = channel.getMemberList().stream()
                .anyMatch(uuid -> uuid.equals(messageCreateRequest.writerId()));

        if(channel.getType() == ChannelType.PRIVATE && !isMember){
            throw new IllegalArgumentException("PRIVATE 채널의 멤버가 아닙니다.");
        }

        List<UUID> binaryContentIds = binaryContentCreateRequests.stream()
                .map(req -> {
                    String fileName = req.fileName();
                    String contentType = req.contentType();
                    byte[] file = req.file();

                    BinaryContent binaryContent = new BinaryContent(fileName, contentType, file);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message message = new Message(
                messageCreateRequest.channelId(),
                messageCreateRequest.content(),
                messageCreateRequest.writerId(),
                binaryContentIds);
        return messageRepository.save(message);
    }

    @Override
    public MessageDTO findById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

        return new MessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getWriterId(),
                message.getContent(),
                message.getAttachmentIds());
    }

    @Override
    public List<MessageDTO> findByChannel(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getChannelId(),
                        message.getWriterId(),
                        message.getContent(),
                        message.getAttachmentIds()
                ))
                .toList();
    }

    @Override
    public List<MessageDTO> findByUser(UUID userId) {
        List<Message> messages = messageRepository.findByUserId(userId);
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getChannelId(),
                        message.getWriterId(),
                        message.getContent(),
                        message.getAttachmentIds()
                ))
                .toList();
    }


    @Override
    public Message update(UUID messageId, UUID channelId, UUID writerId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

        if (!message.getWriterId().equals(writerId)) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }

        message.update(messageUpdateRequest.newContent());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId, UUID writerId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

        if (!message.getWriterId().equals(writerId)) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }

        message.getAttachmentIds()
                        .forEach(binaryContentRepository::deleteById);

        messageRepository.delete(messageId);
    }
}

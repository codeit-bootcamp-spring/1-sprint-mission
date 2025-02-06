package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageDto create(MessageCreateRequest request) {
        Message message = new Message(request.message(), request.userId(), request.channelId(), request.content());
        messageRepository.save(message);

        if (request.content() != null) {
            for (byte[] content : request.content()){
                BinaryContent binaryContent = new BinaryContent(message.getAuthorId(), message.getId(), content);
                binaryContentRepository.save(binaryContent);
            }
        }
        messageRepository.save(message);
        return changeToDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messages.stream()
                .map(message -> changeToDto(message))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {
        Optional<Message> message = messageRepository.findByMessageId(request.messageId());
        if (message.isEmpty()) {
            throw new NotfoundIdException("Message not found");
        }
        List<BinaryContent> existingBinaryContents = binaryContentRepository.findByAllMessageId(request.messageId());
        Set<UUID> existingContentIds = existingBinaryContents.stream()
                .map(BinaryContent::getId)
                .collect(Collectors.toSet());

        // 삭제할 파일이 있다면 삭제
        if (request.deletedContentIds() != null) {
            for (UUID contentId : request.deletedContentIds()) {
                if (existingContentIds.contains(contentId)) {
                    binaryContentRepository.deleteByContentId(contentId);
                }
            }
        }

        // 새로운 BinaryContent 추가
        List<byte[]> updatedContentList = existingBinaryContents.stream()
                .filter(content -> !request.deletedContentIds().contains(content.getId())) // 삭제 안 된 파일 유지
                .map(BinaryContent::getContent)
                .collect(Collectors.toList());

        if (request.newContent() != null) {
            for (byte[] content : request.newContent()) {
                BinaryContent binaryContent = new BinaryContent(message.get().getAuthorId(), message.get().getId(), content);
                binaryContentRepository.save(binaryContent);
                updatedContentList.add(content); // 새 파일 추가
            }
        }

        // 메시지 업데이트 (텍스트 & 남아있는 파일 목록)
        message.get().update(request.newMessage(), updatedContentList);
        messageRepository.save(message.orElse(null));

        return changeToDto(message.orElse(null));
    }

    @Override
    public void delete(UUID messageId) {
        List<BinaryContent> findBinaryContent = binaryContentRepository.findByAllMessageId(messageId);
        for (BinaryContent b: findBinaryContent ){
            binaryContentRepository.deleteByContentId(b.getId());
        }
        messageRepository.deleteByMessageId(messageId);
    }

    private static MessageDto changeToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getMessage(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
